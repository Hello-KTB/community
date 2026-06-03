package ktb4.community.repository;

import ktb4.community.entity.PostLike;
import ktb4.community.entity.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostlikeRepository extends JpaRepository<PostLike, PostLikeId> {
}
