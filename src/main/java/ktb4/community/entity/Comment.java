package ktb4.community.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id",  nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "comment_content", nullable = false)
    private String content;

    @Column(name = "comment_createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "comment_updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    // 기본 생성자
    protected Comment() {}

    // 매개변수 생성자
    public Comment(User author, Post post, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.author = author;
        this.post = post;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}