package ktb4.community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false, unique = true)
    private Long postId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "post_name", nullable = false)
    private String postName;
    @Column(name = "post_content", nullable = false)
    private String postContent;
    @Column(name = "post_image")
    private String postImage;
    @Column(name = "post_createdAt", nullable = false)
    private LocalDateTime postCreatedAt;
    @Column(name = "post_updatedAt", nullable = false)
    private LocalDateTime postUpdatedAt;
    @Column(name = "post_views", nullable = false)
    private int postViews;

    // 기본 생성자
    protected Post() {}

    public Post(Long userId,String postName, String postContent, String postImage, int postViews, LocalDateTime postCreatedAt, LocalDateTime postUpdatedAt) {
        this.userId = userId;
        this.postName = postName;
        this.postContent = postContent;
        this.postImage = postImage;
        this.postViews = postViews;
        this.postCreatedAt = postCreatedAt;
        this.postUpdatedAt = postUpdatedAt;
    }
}