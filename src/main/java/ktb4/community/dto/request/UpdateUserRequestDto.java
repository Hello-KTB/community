package ktb4.community.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UpdateUserRequestDto {
    private String nickname;
    private String profileImage;
}
