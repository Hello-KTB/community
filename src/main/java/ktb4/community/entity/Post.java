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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @Column(name = "post_name", nullable = false)
    private String title;

    @Column(name = "post_content", nullable = false)
    private String content;

    @Column(name = "post_image")
    private String image;

    @Column(name = "post_createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "post_updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "post_views", nullable = false)
    private int views;

    // 기본 생성자
    protected Post() {}

    public Post(User author, String title, String content, String image, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.views = 0;
    }
}