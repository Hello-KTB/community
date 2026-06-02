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
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "post_id", nullable = false)
    private Long postId;
    @Column(name = "comment_content", nullable = false)
    private String commentContent;
    @Column(name = "comment_createdAt", nullable = false)
    private LocalDateTime commentCreatedAt;
    @Column(name = "comment_updatedAt", nullable = false)
    private LocalDateTime commentUpdatedAt;

    // 기본 생성자
    protected Comment() {}

    // 매개변수 생성자
    public Comment(Long userId, Long postId, String commentContent, LocalDateTime commentCreatedAt, LocalDateTime commentUpdatedAt) {
        this.userId = userId;
        this.postId = postId;
        this.commentContent = commentContent;
        this.commentCreatedAt = commentCreatedAt;
        this.commentUpdatedAt = commentUpdatedAt;
    }
}