package ktb4.community.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;
    private Long user_id;

    protected PostLike() {}

    public PostLike(Long post_id, Long user_id) {
        this.post_id = post_id;
        this.user_id = user_id;
    }
}
