package ktb4.community.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id",  nullable = false, unique = true)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User commentAuthor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post commentPost;

    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @Column(name = "comment_createdAt", nullable = false)
    private LocalDateTime commentCreatedAt;

    @Column(name = "comment_updatedAt", nullable = false)
    private LocalDateTime commentUpdatedAt;

    // 기본 생성자
    protected Comment() {}

    // 매개변수 생성자
    public Comment(User commentAuthor, Post commentPost, String commentContent, LocalDateTime commentCreatedAt, LocalDateTime commentUpdatedAt) {
        this.commentAuthor = commentAuthor;
        this.commentPost = commentPost;
        this.commentContent = commentContent;
        this.commentCreatedAt = commentCreatedAt;
        this.commentUpdatedAt = commentUpdatedAt;
    }
}