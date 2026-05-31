package ktb4.community.dto.response;

import lombok.Getter;
import ktb4.community.entity.User;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String image;

    public UserResponseDto(User user) {
        this.id = user.getUserId();
        this.email = user.getUserEmail();
        this.nickname = user.getUserNickname();
        this.image = user.getUserImage();
    }
}