package ktb4.community.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Data
public class Post {
    private Long postId;
    private Long userId;
    private String postName;
    private String postContent;
    private String postImage;
    private int postLikes;
    private int postViews;
    private Date postCreatedAt;
    private Date postUpdatedAt;

    // 기본 생성자
    public Post() {}

    // 매개변수 생성자
    public Post(Long postId, Long userId,String postName, String postContent, String postImage, int postLikes, int postViews, Date postCreatedAt, Date postUpdatedAt) {
        this.postId = postId;
        this.userId = userId;
        this.postName = postName;
        this.postContent = postContent;
        this.postImage = postImage;
        this.postLikes = postLikes;
        this.postViews = postViews;
        this.postCreatedAt = postCreatedAt;
        this.postUpdatedAt = postUpdatedAt;
    }

    public void changePostName(String postName) {
        this.postName = postName;
    }

    public void changePostContent(String postContent) {
        this.postContent = postContent;
    }

    public void changePostImage(String postImage) {
        this.postImage = postImage;
    }

    public void updatePostUpdatedAt(Date postUpdatedAt) {
        this.postUpdatedAt = postUpdatedAt;
    }
}