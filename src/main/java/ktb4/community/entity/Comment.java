package ktb4.community.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private Long userId;
    private Long postId;
    private String commentContent;
    private int commentLikes;
    private LocalDateTime commentCreatedAt;
    private LocalDateTime commentUpdatedAt;

    // 기본 생성자
    protected Comment() {}

    // 매개변수 생성자
    public Comment(Long userId, Long postId, String commentContent,  int commentLikes, LocalDateTime commentCreatedAt, LocalDateTime commentUpdatedAt) {
        this.userId = userId;
        this.postId = postId;
        this.commentContent = commentContent;
        this.commentLikes = commentLikes;
        this.commentCreatedAt = commentCreatedAt;
        this.commentUpdatedAt = commentUpdatedAt;
    }
}