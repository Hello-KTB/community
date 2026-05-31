package ktb4.community.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
/*
RequestDto의
 */
public class UserRequestDto {
    private String userEmail;
    private String userPassword;
    private String userNickname;
    private String userImage;
}