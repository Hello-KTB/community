package ktb4.community.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String url;

    @Column(name = "image_type", nullable = false)
    private String type;

    @Column(name = "image_createdAt", nullable = false)
    private LocalDateTime createdAt;

    public Image(String url, String type) {
        this.url = url;
        this.type = type;
        this.createdAt = LocalDateTime.now();
    }
}
