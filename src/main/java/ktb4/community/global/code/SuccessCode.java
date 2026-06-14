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
    CHECK_NICKNAME_SUCCESS(HttpStatus.OK, "사용 가능한 닉네임입니다"),    // 추가

    // 게시물
    CREATE_POST_SUCCESS(HttpStatus.CREATED, "게시물이 등록되었습니다"),
    UPDATE_POST_SUCCESS(HttpStatus.OK, "게시물을 수정하였습니다"),
    GET_POSTS_SUCCESS(HttpStatus.OK, "게시물 목록을 조회합니다"),
    GET_POST_SUCCESS(HttpStatus.OK, "게시물을 조회합니다"),
    PRESS_LIKE_SUCCESS(HttpStatus.OK, "좋아요를 눌렀습니다"),
    UNDO_LIKE_SUCCESS(HttpStatus.OK, "좋아요를 취소했습니다"),

    // 댓글
    CREATE_COMMENT_SUCCESS(HttpStatus.CREATED, "댓글을 등록했습니다"),
    UPDATE_COMMENT_SUCCESS(HttpStatus.OK, "댓글이 수정되었습니다"),
    GET_COMMENTS_SUCCESS(HttpStatus.OK, "댓글 목록을 조회합니다"),

    // 이미지
    IMAGE_UPLOAD_SUCCESS(HttpStatus.OK, "이미지 업로드에 성공하셨습니다");

    private final HttpStatus status;
    private final String message;
}