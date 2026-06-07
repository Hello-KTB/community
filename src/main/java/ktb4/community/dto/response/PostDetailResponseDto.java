package ktb4.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

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
    private int views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
