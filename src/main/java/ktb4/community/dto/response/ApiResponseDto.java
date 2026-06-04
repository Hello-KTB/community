package ktb4.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponseDto<T> {
    private int statusCode;
    private boolean success;
    private String message;
    private T data;
}
