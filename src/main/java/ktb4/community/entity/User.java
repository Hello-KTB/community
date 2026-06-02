package ktb4.community.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", nullable = false, unique = true)
    private Long userId;
    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;
    @Column(name = "user_nickname", nullable = false, unique = true)
    private String userNickname;
    @Column(name = "user_password", nullable = false)
    private String userPassword;
    @Column(name = "user_image")
    private String userImage;
    @Column(name = "user_createdAt")
    private LocalDateTime userCreatedAt;
    @Column(name = "user_updatedAt")
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