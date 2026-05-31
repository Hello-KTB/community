package ktb4.community.entity;

import lombok.Data;

@Data
public class PostLike {
    private Long post_id;
    private Long user_id;

    public PostLike() {}

    public PostLike(Long post_id, Long user_id) {
        this.post_id = post_id;
        this.user_id = user_id;
    }
}
