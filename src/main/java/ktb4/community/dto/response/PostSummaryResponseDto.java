package ktb4.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostSummaryResponseDto {
    private Long id;
    private String title;
    private AuthorResponseDto author;
    private long likeCount;
    private long commentCount;
    private long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
