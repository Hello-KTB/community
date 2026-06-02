package ktb4.community.dto.request;

import java.time.LocalDateTime;

public class UserRequestDto {
    private String userEmail;
    private String userPassword;
    private String userNickname;
    private String userImage;
    private LocalDateTime userCreatedAt;
    private LocalDateTime userUpdatedAt;
}