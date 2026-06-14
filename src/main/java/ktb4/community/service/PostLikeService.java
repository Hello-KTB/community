package ktb4.community.service;

import ktb4.community.dto.response.PostLikeResponseDto;
import ktb4.community.entity.PostLike;
import ktb4.community.entity.PostLikeId;
import ktb4.community.global.code.ErrorCode;
import ktb4.community.global.exception.CustomException;
import ktb4.community.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

// 게시물 좋아요 관련 비즈니스 로직을 처리하는 서비스 계층
@Service
@RequiredArgsConstructor
// 기본적으로 읽기 전용 트랜잭션 적용 (SELECT 최적화)
// 데이터 변경이 필요한 메서드에는 개별적으로 @Transactional 적용
@Transactional(readOnly = true)
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;

    /**
     * 복합키로 좋아요 조회
     * 존재하지 않는 경우 예외 발생
     * cancelLike()에서 삭제 전 존재 여부 확인 시 사용
     *
     * 파라미터 : postLikeId (postId + userId) 복합키
     * 반환 : 조회된 PostLike 엔티티
     * @throws CustomException : 존재하지 않는 좋아요일 경우 (LIKE_NOT_FOUND)
     */
    public PostLike findById(PostLikeId postLikeId) {
        return postLikeRepository.findById(postLikeId)
                .orElseThrow(() -> new CustomException(ErrorCode.LIKE_NOT_FOUND));
    }

    /**
     * 좋아요 추가
     * 이미 좋아요를 누른 경우 409 Conflict 예외 발생 (중복 방지)
     * new PostLike()로 만든 객체는 비영속 상태이므로 save()를 명시적으로 호출해야 INSERT가 됨
     * 저장 후 해당 게시물의 전체 좋아요 수를 COUNT 쿼리로 계산하여 반환
     *
     * 파라미터 : post_id 좋아요를 누를 게시물 ID
     * 파라미터 : user_id 토큰에서 추출한 요청자 ID
     * 반환 : isLiked(true)와 현재 좋아요 수를 담은 PostLikeResponseDto
     * @throws CustomException : 이미 좋아요를 누른 경우 (ALREADY_LIKED)
     */
    @Transactional
    public PostLikeResponseDto pressLike(Long post_id, Long user_id) {
        PostLike postLike = new PostLike(new PostLikeId(post_id, user_id));

        // 중복 좋아요 방지 (DB 복합키로도 막히지만 명시적으로 검증)
        if (postLikeRepository.existsByPostLikeId(postLike.getPostLikeId())) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }
        // 비영속 상태 객체이므로 save() 호출 필요
        postLikeRepository.save(postLike);

        // 저장 후 현재 좋아요 수 계산
        long likes = postLikeRepository.countByPostLikeId_PostId(post_id);
        return new PostLikeResponseDto(true, likes);
    }

    /**
     * 좋아요 취소
     * 좋아요가 존재하지 않으면 404 예외 발생
     * 삭제 후 해당 게시물의 전체 좋아요 수를 COUNT 쿼리로 계산하여 반환
     *
     * 파라미터 : postLikeId (postId + userId) 복합키
     * 반환 : isLiked(false)와 현재 좋아요 수를 담은 PostLikeResponseDto
     * @throws CustomException : 좋아요가 존재하지 않을 경우 (LIKE_NOT_FOUND)
     */
    @Transactional
    public PostLikeResponseDto cancelLike(PostLikeId postLikeId) {
        PostLike postLike = findById(postLikeId);
        postLikeRepository.delete(postLike);

        // 삭제 후 현재 좋아요 수 계산
        long likes = postLikeRepository.countByPostLikeId_PostId(postLikeId.getPostId());
        return new PostLikeResponseDto(false, likes);
    }
}