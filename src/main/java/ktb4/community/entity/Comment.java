package ktb4.community.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class Comment {
    private Long commentId;
    private Long userId;
    private Long postId;
    private String commentContent;
    private int commentLikes;
    private Date commentCreatedAt;
    private Date commentUpdatedAt;

    // 기본 생성자
    public Comment() {}

    // 매개변수 생성자
    public Comment(Long commentId, Long userId, Long postId, String commentContent,  int commentLikes, Date commentCreatedAt, Date commentUpdatedAt) {
        this.commentId = commentId;
        this.userId = userId;
        this.postId = postId;
        this.commentContent = commentContent;
        this.commentLikes = commentLikes;
        this.commentCreatedAt = commentCreatedAt;
        this.commentUpdatedAt = commentUpdatedAt;
    }

    public void changeCommentContent(String postContent) {
        this.commentContent = postContent;
    }

    public void updateCommentUpdatedAt(Date commentUpdatedAt) {
        this.commentUpdatedAt = commentUpdatedAt;
    }
}