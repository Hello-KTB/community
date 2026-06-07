package ktb4.community.service;

import ktb4.community.dto.request.CommentRequestDto;
import ktb4.community.dto.response.CommentResponseDto;
import ktb4.community.entity.Comment;
import ktb4.community.entity.Post;
import ktb4.community.entity.User;
import ktb4.community.repository.CommentRepository;
import ktb4.community.repository.PostRepository;
import ktb4.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

// 댓글 관련 비즈니스 로직을 처리하는 서비스 계층
@Service
@RequiredArgsConstructor
// 기본적으로 읽기 전용 트랜잭션 적용 (SELECT 최적화)
// 데이터 변경이 필요한 메서드에는 개별적으로 @Transactional 적용
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 생성
     * 토큰에서 추출한 userId로 작성자를, postId로 게시물을 조회한 후 댓글 생성 및 저장
     * new Comment()로 만든 객체는 비영속 상태이므로 save()를 명시적으로 호출해야 INSERT가 됨
     * 엔티티를 직접 반환하면 지연 로딩 프록시 직렬화 에러가 발생하므로 DTO로 변환 후 반환
     *
     * 파라미터 : postId  댓글을 작성할 게시물 ID
     * 파라미터 : userId  토큰에서 추출한 작성자 ID
     * 파라미터 : request 댓글 내용을 담은 요청 DTO
     * 반환 : 저장된 댓글 정보를 담은 CommentResponseDto
     * @throws IllegalArgumentException : 존재하지 않는 회원 또는 게시물일 경우
     */
    @Transactional
    public CommentResponseDto create(Long postId, Long userId, CommentRequestDto request) {
        // userId로 작성자 조회 (없으면 예외)
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));

        // postId로 게시물 조회 (없으면 예외)
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다"));

        // 내용이 없으면 생성 불가
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글을 입력하세요");
        }

        // 비영속 상태 객체이므로 save() 호출 필요
        Comment comment = new Comment(author, post, request.getContent(), LocalDateTime.now(), LocalDateTime.now());
        commentRepository.save(comment);

        // 엔티티 → DTO 변환 후 반환
        return new CommentResponseDto(
                comment.getId(),
                comment.getAuthor().getNickname(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    /**
     * ID로 댓글 조회
     * 존재하지 않는 ID 요청 시 예외 발생
     *
     * 파라미터 : id 조회할 댓글 ID
     * 반환 : 조회된 Comment 엔티티
     * @throws IllegalArgumentException : 존재하지 않는 댓글일 경우
     */
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다"));
    }

    /**
     * 댓글 수정
     * 작성자 본인만 수정 가능 (userId 검증)
     * findById()로 가져온 객체는 영속 상태이므로 @Transactional 종료 시 변경 감지로 자동 UPDATE
     * Comment 엔티티의 updateContent() 도메인 메서드를 통해 updatedAt도 함께 갱신
     *
     * 파라미터 : id      수정할 댓글 ID
     * 파라미터 : userId  토큰에서 추출한 요청자 ID (작성자 검증용)
     * 파라미터 : request 변경할 댓글 내용을 담은 요청 DTO
     * 반환 : 수정된 댓글 정보를 담은 CommentResponseDto
     * @throws ResponseStatusException : 작성자가 아닐 경우 403
     */
    @Transactional
    public CommentResponseDto update(Long id, Long userId, CommentRequestDto request) {
        Comment comment = findById(id);
        // 작성자 본인 여부 검증
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 댓글의 게시자가 아닙니다");
        }
        // 내용이 없으면 수정 불가
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글을 수정하세요");
        }
        // 도메인 메서드를 통해 수정 (updatedAt 자동 갱신)
        comment.updateContent(request.getContent());

        // 엔티티 → DTO 변환 후 반환
        return new CommentResponseDto(
                comment.getId(),
                comment.getAuthor().getNickname(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    /**
     * 댓글 삭제
     * 작성자 본인만 삭제 가능 (userId 검증)
     *
     * 파라미터 : id     삭제할 댓글 ID
     * 파라미터 : userId 토큰에서 추출한 요청자 ID (작성자 검증용)
     * @throws ResponseStatusException : 작성자가 아닐 경우 403
     */
    @Transactional
    public void delete(Long id, Long userId) {
        Comment comment = findById(id);
        // 작성자 본인 여부 검증
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 댓글의 게시자가 아닙니다");
        }
        commentRepository.delete(comment);
    }

    /**
     * 게시물별 댓글 목록 조회 (Slice 페이징)
     * 특정 게시물의 댓글을 최신순으로 페이징하여 반환
     * Slice는 COUNT 쿼리 없이 다음 페이지 존재 여부만 판단 → 무한 스크롤에 적합
     * 엔티티를 직접 반환하면 지연 로딩 프록시 직렬화 에러가 발생하므로 DTO로 변환
     *
     * 파라미터 : postId 댓글을 조회할 게시물 ID
     * 파라미터 : page   페이지 번호 (0부터 시작)
     * 파라미터 : size   한 페이지당 댓글 수
     * 반환 : CommentResponseDto의 Slice
     * @throws IllegalArgumentException : 존재하지 않는 게시물일 경우
     */
    public Slice<CommentResponseDto> getComments(Long postId, int page, int size) {
        // postId로 게시물 조회 (없으면 예외)
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다"));
        Pageable pageable = PageRequest.of(page, size);

        // 엔티티 → DTO 변환 (지연 로딩 프록시 직렬화 에러 방지)
        return commentRepository.findByPostOrderByCreatedAtDesc(post, pageable)
                .map(comment -> new CommentResponseDto(
                        comment.getId(),
                        comment.getAuthor().getNickname(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getUpdatedAt()
                ));
    }
}