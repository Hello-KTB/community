package ktb4.community.global.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 공통 에러
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값을 확인해주세요"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생헀습니다"),

    // 유저 관련 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다"),
    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다"),
    ALREADY_EXIST_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다"),
    SAME_AS_CURRENT_PASSWORD(HttpStatus.CONFLICT, "현재 비밀번호와 동일합니다"),

    // 게시물 관련 에러
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시물입니다"),
    POST_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "게시물 수정 권한이 없습니다"),
    POST_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "게시물 삭제 권한이 없습니다"),
    POST_NO_CONTENT(HttpStatus.BAD_REQUEST, "수정할 내용을 입력해주세요"),

    // 좋아요 관련 에러
    ALREADY_LIKED(HttpStatus.CONFLICT, "이미 좋아요를 누른 게시물입니다"),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요가 존재하지 않습니다"),

    // 댓글 관련 에러
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다"),
    COMMENT_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "댓글 수정 권한이 없습니다"),
    COMMENT_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "댓글 삭제 권한이 없습니다");

    // 기본 필드
    private final HttpStatus status;
    private final String message;
}