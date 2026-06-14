package ktb4.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreatePostResponseDto {
    Long insertId;
    String title;
    String author;
    String content;
    String image;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}