package ktb4.community.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
@EqualsAndHashCode
public class PostLikeId {
    @Column(name = "post_id")
    private Long postId;
    @Column(name = "user_id")
    private Long userId;

    protected PostLikeId() {}

    public PostLikeId(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
