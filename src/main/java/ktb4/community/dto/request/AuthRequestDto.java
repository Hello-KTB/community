package ktb4.community.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthRequestDto {
    private String email;
    private String password;
}
