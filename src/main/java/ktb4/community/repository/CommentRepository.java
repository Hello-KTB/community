package ktb4.community.repository;

import ktb4.community.entity.Comment;
import ktb4.community.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Slice<Comment> findByPostOrderByCreatedAtDesc(Post post, Pageable pageable);
}