package ktb4.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostDetailResponseDto {
    private Long id;
    private String title;
    private String nickname;
    private String profileImage;
    private Long writerId;
    private String content;
    private String image;
    private boolean isLiked;
    private long likeCount;
    private long commentCount;
    private long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}