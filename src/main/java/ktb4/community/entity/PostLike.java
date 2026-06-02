package ktb4.community.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
public class PostLike {
    @Id @Column(name = "post_id", nullable = false)
    private Long postId;
    @Id @Column(name = "user_id", nullable = false)
    private Long userId;

    protected PostLike() {}

    public PostLike(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
