package ktb4.community.repository;

import ktb4.community.entity.PostLike;
import ktb4.community.entity.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    long countByPostLikeId_PostId(Long postId);
    boolean existsByPostLikeId(PostLikeId postLikeId);
}
