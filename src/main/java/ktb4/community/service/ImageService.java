package ktb4.community.service;

import ktb4.community.entity.Image;
import ktb4.community.global.code.ErrorCode;
import ktb4.community.global.exception.CustomException;
import ktb4.community.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    @Value("${file.path}")
    private String uploadPath;

    @Transactional
    public String uploadImage(MultipartFile image, String type) {
        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid.toString() + "_" + image.getOriginalFilename();
        Path imageFilePath = Paths.get(uploadPath, imageFileName);
        try {
            Files.copy(image.getInputStream(), imageFilePath);
        } catch(IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        String imageUrl = "/uploads/"+imageFileName;
        imageRepository.save(new Image(imageUrl, type));

        return imageUrl;
    }
}
