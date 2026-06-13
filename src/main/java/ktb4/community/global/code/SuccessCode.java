package ktb4.community.global.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    // 유저
    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입에 성공하셨습니다"),
    UPDATE_USER_SUCCESS(HttpStatus.OK, "회원정보가 수정되었습니다"),
    UPDATE_PASSWORD_SUCCESS(HttpStatus.OK, "비밀번호가 변경되었습니다"),
    DELETE_USER_SUCCESS(HttpStatus.OK, "회원 탈퇴가 완료되었습니다"),
    CHECK_EMAIL_SUCCESS(HttpStatus.OK, "사용 가능한 이메일입니다"),       // 추가
    CHECK_NICKNAME_SUCCESS(HttpStatus.OK, "사용 가능한 닉네임입니다");    // 추가

    private final HttpStatus status;
    private final String message;
}
