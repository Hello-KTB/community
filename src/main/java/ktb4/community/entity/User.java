package ktb4.community.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userEmail;
    private String userPassword;
    private String userNickname;
    private String userImage;
    private LocalDateTime userCreatedAt;
    private LocalDateTime userUpdatedAt;

    protected User() {}

    public User(String userEmail, String userPassword, String userNickname, String userImage) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userNickname = userNickname;
        this.userImage = userImage;
        this.userCreatedAt = LocalDateTime.now();
        this.userUpdatedAt = LocalDateTime.now();
    }
}