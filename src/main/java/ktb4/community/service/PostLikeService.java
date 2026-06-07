package ktb4.community.service;

import ktb4.community.dto.response.PostLikeResponseDto;
import ktb4.community.entity.PostLike;
import ktb4.community.entity.PostLikeId;
import ktb4.community.repository.PostRepository;
import ktb4.community.repository.PostLikeRepository;
import ktb4.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;

    public PostLike findById(PostLikeId postLikeId) {
        return postLikeRepository.findById(postLikeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 좋아요입니다"));
    }

    @Transactional
    public PostLikeResponseDto pressLike(Long post_id, Long user_id) {
        PostLike postLike = new PostLike(new PostLikeId(post_id, user_id));

        if (postLikeRepository.existsByPostLikeId(postLike.getPostLikeId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 좋아요를 눌렀습니다");
        }
        postLikeRepository.save(postLike);
        long likes = postLikeRepository.countByPostLikeId_PostId(post_id);
        return new PostLikeResponseDto(true, likes);
    }

    @Transactional
    public PostLikeResponseDto cancelLike(PostLikeId postLikeId) {
        PostLike postLike = findById(postLikeId);
        postLikeRepository.delete(postLike);
        long likes = postLikeRepository.countByPostLikeId_PostId(postLikeId.getPostId());
        return new PostLikeResponseDto(false, likes);
    }
}
