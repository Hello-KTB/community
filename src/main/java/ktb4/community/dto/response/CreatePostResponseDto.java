package ktb4.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreatePostResponseDto {
    Long postId;
    String title;
    String nickname;
    String content;
    String image;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
