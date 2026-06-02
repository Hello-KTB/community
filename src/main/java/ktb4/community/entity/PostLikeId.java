package ktb4.community.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class PostLikeId {

    private Post post;
    private User user;

    protected PostLikeId() {}

    public PostLikeId(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}
