package ktb4.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostLikeResponseDto {
    boolean isLiked;
    long likes;
}
