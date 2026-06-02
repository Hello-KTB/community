package ktb4.community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User postAuthor;

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

    public Post(User postAuthor, String postName, String postContent, String postImage, LocalDateTime postCreatedAt, LocalDateTime postUpdatedAt) {
        this.postAuthor = postAuthor;
        this.postName = postName;
        this.postContent = postContent;
        this.postImage = postImage;
        this.postCreatedAt = postCreatedAt;
        this.postUpdatedAt = postUpdatedAt;
        this.postViews = 0;
    }

    public static Post createPost(String userId, String postName, String postContent, String postImage, LocalDateTime currentTime) {
        if(postName == null || postName.isBlank()) throw new IllegalArgumentException("게시물 제목을 입력해주세요");
        if (postContent == null || postContent.isBlank()) throw new IllegalArgumentException("게시물 내용을 입력해주세요");

        return new Post();
    }
}