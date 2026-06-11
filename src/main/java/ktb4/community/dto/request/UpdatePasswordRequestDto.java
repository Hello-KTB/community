package ktb4.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UpdatePasswordRequestDto {
    @NotBlank(message = "새 비밀번호를 입력해주세요")
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%]).+$")
    private String password;
}