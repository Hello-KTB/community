package ktb4.community.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private Long userId;
    private String postName;
    private String postContent;
    private String postImage;
    private int postLikes;
    private int postViews;
    private LocalDateTime postCreatedAt;
    private LocalDateTime postUpdatedAt;

    // 기본 생성자
    protected Post() {}

    public Post(Long userId,String postName, String postContent, String postImage, int postLikes, int postViews, LocalDateTime postCreatedAt, LocalDateTime postUpdatedAt) {
        this.userId = userId;
        this.postName = postName;
        this.postContent = postContent;
        this.postImage = postImage;
        this.postLikes = postLikes;
        this.postViews = postViews;
        this.postCreatedAt = postCreatedAt;
        this.postUpdatedAt = postUpdatedAt;
    }
}