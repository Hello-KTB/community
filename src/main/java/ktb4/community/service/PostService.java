package ktb4.community.service;

import ktb4.community.dto.request.CreatePostRequestDto;
import ktb4.community.dto.response.*;
import ktb4.community.entity.Post;
import ktb4.community.entity.PostLikeId;
import ktb4.community.entity.User;
import ktb4.community.global.code.ErrorCode;
import ktb4.community.global.exception.CustomException;
import ktb4.community.repository.CommentRepository;
import ktb4.community.repository.PostLikeRepository;
import ktb4.community.repository.PostRepository;
import ktb4.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 게시물 관련 비즈니스 로직을 처리하는 서비스 계층
@Service
@RequiredArgsConstructor
// 기본적으로 읽기 전용 트랜잭션 적용 (SELECT 최적화)
// 데이터 변경이 필요한 메서드에는 개별적으로 @Transactional 적용
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    /**
     * 게시물 생성
     * 토큰에서 추출한 userId로 작성자를 조회한 후 게시물 생성 및 저장
     * new Post()로 만든 객체는 비영속 상태이므로 save()를 명시적으로 호출해야 INSERT가 됨
     *
     * 파라미터 : userId  토큰에서 추출한 작성자 ID
     * 파라미터 : request 제목, 내용, 이미지를 담은 요청 DTO
     * 반환 : 저장된 Post 엔티티
     * @throws IllegalStateException : 존재하지 않는 회원일 경우
     */
    @Transactional
    public CreatePostResponseDto create(Long userId, CreatePostRequestDto request) {
        // userId로 작성자 조회 (없으면 예외)
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 비영속 상태 객체이므로 save() 호출 필요
        Post post = new Post(author, request.getTitle(), request.getContent(), request.getImage(), LocalDateTime.now(), LocalDateTime.now());
        postRepository.save(post);

        // 트랜잭션 안에서 변환 (지연 로딩 프록시 초기화 가능)
        return new CreatePostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getAuthor().getNickname(),
                post.getContent(),
                post.getImage(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    /**
     * ID로 게시물 조회
     * 존재하지 않는 ID 요청 시 예외 발생
     *
     * 파라미터 : id 조회할 게시물 ID
     * 반환 : 조회된 Post 엔티티
     * @throws IllegalArgumentException : 존재하지 않는 게시물일 경우
     */
    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    /**
     * 게시물 목록 조회 (Slice 페이징)
     * offset/limit 방식으로 요청받아 page/size로 변환 후 최신순 정렬
     * Slice는 COUNT 쿼리 없이 다음 페이지 존재 여부만 판단 → 무한 스크롤에 적합
     * Post 엔티티를 그대로 반환하면 지연 로딩 프록시 직렬화 에러가 발생하므로 DTO로 변환
     *
     * 파라미터 : offset 시작 위치 (0부터 시작)
     * 파라미터 : limit  한 번에 가져올 개수
     * 반환 : PostSummaryResponseDto의 Slice
     */
    public Slice<PostSummaryResponseDto> getPostsWithPaging(int offset, int limit) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));
        // offset/limit → page/size 변환
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(sorts));
        Slice<Post> posts = postRepository.findAllBy(pageable);

        // 엔티티 → DTO 변환 (지연 로딩 프록시 직렬화 에러 방지)
        return posts.map(post -> new PostSummaryResponseDto(
                post.getId(),
                post.getTitle(),
                new AuthorResponseDto(post.getAuthor().getNickname(), post.getAuthor().getProfileImage()),
                postLikeRepository.countByPostLikeId_PostId(post.getId()),
                commentRepository.countByPostId(post.getId()),
                post.getViews(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        ));
    }

    /**
     * 특정 게시물 상세 조회
     * 댓글 목록은 별도 API로 분리되어 있으므로 여기서는 포함하지 않음
     * Post 엔티티를 그대로 반환하면 지연 로딩 프록시 직렬화 에러가 발생하므로 DTO로 변환
     *
     * 파라미터 : id 조회할 게시물 ID
     * 반환 : PostDetailResponseDto
     */
    @Transactional
    public PostDetailResponseDto getPostDetail(Long postId, Long userId) {
        Post post = findById(postId);
        post.increaseViews();

        // 엔티티 → DTO 변환 (지연 로딩 프록시 직렬화 에러 방지)
        return new PostDetailResponseDto(
                post.getId(),
                post.getTitle(),
                post.getAuthor().getNickname(),
                post.getAuthor().getProfileImage(),
                post.getAuthor().getId(),
                post.getContent(),
                post.getImage(),
                postLikeRepository.existsByPostLikeId(new PostLikeId(postId, userId)),
                postLikeRepository.countByPostLikeId_PostId(postId),
                commentRepository.countByPostId(postId),
                post.getViews(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    /**
     * 게시물 수정
     * 작성자 본인만 수정 가능 (userId 검증)
     * findById()로 가져온 객체는 영속 상태이므로 @Transactional 종료 시 변경 감지로 자동 UPDATE
     * Post 엔티티의 update() 도메인 메서드를 통해 updatedAt도 함께 갱신
     *
     * 파라미터 : id     수정할 게시물 ID
     * 파라미터 : userId 토큰에서 추출한 요청자 ID (작성자 검증용)
     * 파라미터 : title  변경할 제목
     * 파라미터 : content 변경할 내용
     * 파라미터 : image  변경할 이미지
     * 반환 : 수정된 Post 엔티티
     * @throws ResponseStatusException : 작성자가 아닐 경우 403
     */
    @Transactional
    public UpdatePostResponseDto update(Long postId, Long userId, String title, String content, String image) {
        Post post = findById(postId);
        // 작성자 본인 여부 검증
        if (!post.getAuthor().getId().equals(userId)) {
            throw new CustomException(ErrorCode.POST_UPDATE_FORBIDDEN);
        }
        // 셋 다 null이면 수정할 내용 없음 → 400
        if (title == null && content == null && image == null) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        // 도메인 메서드를 통해 수정 (updatedAt 자동 갱신)
        post.update(title, content, image);
        postRepository.save(post);

        return new UpdatePostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getImage(),
                post.getUpdatedAt()
        );
    }

    /**
     * 게시물 삭제
     * 작성자 본인만 삭제 가능 (userId 검증)
     * DB에 ON DELETE CASCADE가 설정되어 있으므로 해당 게시물의 댓글, 좋아요도 함께 삭제됨
     *
     * 파라미터 : id     삭제할 게시물 ID
     * 파라미터 : userId 토큰에서 추출한 요청자 ID (작성자 검증용)
     * @throws ResponseStatusException : 작성자가 아닐 경우 403
     */
    @Transactional
    public void delete(Long postId, Long userId) {
        Post post = findById(postId);
        // 작성자 본인 여부 검증
        if (!post.getAuthor().getId().equals(userId)) {
            throw new CustomException(ErrorCode.POST_DELETE_FORBIDDEN);
        }
        postRepository.delete(post);
    }
}