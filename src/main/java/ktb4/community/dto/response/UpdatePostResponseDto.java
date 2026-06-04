package ktb4.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UpdatePostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String image;
    private LocalDateTime updatedAt;
}