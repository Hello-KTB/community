package ktb4.community.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@AllArgsConstructor
public class PostLike {
    @EmbeddedId
    private PostLikeId postLikeId;

    protected PostLike() {}
}
