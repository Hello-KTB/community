package ktb4.community.dto.response;

import ktb4.community.entity.Comment;
import ktb4.community.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostDetailResponseDto {
    private Long id;
    private String title;
    private String author;
    private String content;
    private String image;
    private int likes;
    private int comment_count;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Comment> comments;
}
