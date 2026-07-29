package ktb4.community.service;

import io.awspring.cloud.s3.S3Template;
import ktb4.community.dto.response.PresignedUrlResponseDto;
import ktb4.community.global.code.ErrorCode;
import ktb4.community.global.exception.CustomException;
import ktb4.community.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    // Spring Cloud AWS에서 자동 구성되는 S3Template 주입
    private final S3Template s3Template;

    // application-prod.yaml에 정의된 S3 버킷 이름 주입
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    /**
     * S3 직접 업로드용 Presigned URL 생성
     *
     * @param originalFilename 클라이언트가 업로드하려는 원본 파일명
     * @return Presigned URL(PUT)과 업로드 후 접근 가능한 S3 URL이 담긴 DTO
     */
    public PresignedUrlResponseDto generatePresignedUrl(String originalFilename) {
        // 1. 파일명 유효성 검증 (기존 로직 유지)
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_FILE);
        }

        // 2. 확장자 추출 및 허용 여부 검증 (기존 로직 유지)
        String extension = originalFilename
                .substring(originalFilename.lastIndexOf('.') + 1)
                .toLowerCase();
        List<String> allowedExtensions = List.of("jpg", "jpeg", "png", "webp");
        if (!allowedExtensions.contains(extension)) {
            throw new CustomException(ErrorCode.INVALID_FILE);
        }

        // 3. S3 객체 고유 Key(저장 경로 및 파일명) 생성 (예: uploads/uuid.png)
        UUID uuid = UUID.randomUUID();
        String s3Key = "uploads/" + uuid.toString() + "." + extension;

        // 4. Presigned URL 유효 기간 설정 (예: 10분 동안만 사용 가능)
        Duration signatureDuration = Duration.ofMinutes(10);

        // 5. S3Template을 활용해 PUT 요청용 Presigned URL 생성
        URL presignedPutUrl = s3Template.createSignedPutURL(bucketName, s3Key, signatureDuration);

        // 6. 업로드가 완료된 후 DB 저장 및 브라우저 조회에 사용될 S3 최종 URL
        String fileUrl = String.format("https://%s.s3.amazonaws.com/%s", bucketName, s3Key);

        return new PresignedUrlResponseDto(presignedPutUrl.toString(), fileUrl);
    }
}
