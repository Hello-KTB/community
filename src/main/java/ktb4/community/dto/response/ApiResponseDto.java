package ktb4.community.dto.response;

import ktb4.community.global.code.ErrorCode;
import ktb4.community.global.code.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 빌더 애노테이션 적용 (Lombok에서 제공)
 * 어떤 필드에 어떤 값을 넣는지 명시적으로 작성함으로써
 * 생성자보다 가독성이 좋다는 장점이 있음
 */
@Getter @Builder
@AllArgsConstructor
public class ApiResponseDto<T> {
    private int statusCode;
    private boolean success;
    private String message;
    private T data;

    /**
     * 컨트롤러의 중복된 코드에 따른
     * 정적 헬퍼 메서드 적용
     *
     * What is 정적 헬퍼 메서드 : 객체 생성 없이 클래스명으로 바로 호출할 수 있는 메서드
     */
    public static <T> ApiResponseDto<T> success(T data, SuccessCode successCode) {
        return ApiResponseDto.<T>builder()
                .statusCode(successCode.getStatus().value())
                .success(true)
                .message(successCode.getMessage())
                .data(data)
                .build();
    }

    public static <T> ApiResponseDto<T> error(ErrorCode errorCode) {
        return ApiResponseDto.<T>builder()
                .statusCode(errorCode.getStatus().value())
                .success(false)
                .message(errorCode.getMessage())
                .data(null)
                .build();
    }
}