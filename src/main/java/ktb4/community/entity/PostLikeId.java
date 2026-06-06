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
    private Long post_id;
    @Column(name = "user_id")
    private Long user_id;

    protected PostLikeId() {}

    public PostLikeId(Long post_id, Long user_id) {
        this.post_id = post_id;
        this.user_id = user_id;
    }
}
