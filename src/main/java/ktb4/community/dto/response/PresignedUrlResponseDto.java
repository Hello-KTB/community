package ktb4.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresignedUrlResponseDto {
    // S3 버킷으로 직접 파일(PUT 요청)을 업로드할 때 사용할 임시 서명 URL
    private String presignedUrl;

    // 업로드 완료 후 게시글 작성/수정 API 등에 전달하여 DB에 저장할 최종 파일 조회 URL
    private String fileUrl;
}
