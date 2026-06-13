package ktb4.community.global.exception;

import ktb4.community.dto.response.ApiResponseDto;
import ktb4.community.global.code.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 일반적인 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> handleAllExceptions(Exception e) {
        logger.error("An unexpected error occurred: {}", e.getMessage(), e);
        ApiResponseDto apiResponseDto = ApiResponseDto.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .success(false)
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .data(null)
                .build();
        return new ResponseEntity<>(apiResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // CustomException 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponseDto> handleCustomException(CustomException e) {
        logger.error("Resource Not Found: {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponseDto.error(e.getErrorCode()));
    }

    // 유효성 검증 예외 처리 (예: @Valid 어노테이션 사용 시)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto> handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        logger.error("Validation Error: {}", errorMessage);
        ApiResponseDto apiResponseDto = ApiResponseDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .success(false)
                .message(errorMessage)
                .data(null)
                .build();
        return new ResponseEntity<>(apiResponseDto, HttpStatus.BAD_REQUEST);
    }
}
