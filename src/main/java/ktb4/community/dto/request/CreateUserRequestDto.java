package ktb4.community.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CreateUserRequestDto {
    private String email;
    private String password;
    private String validatePassword;
    private String nickname;
    private String profileImage;
}