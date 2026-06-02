package ktb4.community.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
public class PostLike {
    @EmbeddedId
    private PostLikeId postLikeId;

    protected PostLike() {}

    public PostLike(PostLikeId postLikeId) {
        this.postLikeId = postLikeId;
    }
}
