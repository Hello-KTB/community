package ktb4.community.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter @Setter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
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
        if (userEmail == null || userEmail.isBlank()) throw new IllegalArgumentException("이메일을 입력해주세요");
        if (userPassword == null || userPassword.isBlank()) throw new IllegalArgumentException("비밀번호를 입력해주세요");
        if (userNickname == null || userNickname.isBlank()) throw new IllegalArgumentException("닉네임을 입력해주세요");
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userNickname = userNickname;
        this.userImage = userImage;
        this.userCreatedAt = LocalDateTime.now();
        this.userUpdatedAt = LocalDateTime.now();
    }

    public static User createUser(String userEmail, String userPassword, String userNickname, String userImage) {
        return new User(userEmail, userPassword, userNickname, userImage);
    }

    public void changeUserNickname(String newNickname) {
        if(newNickname == null || newNickname.isBlank()) throw new IllegalArgumentException("닉네임을 입력해주세요");
        if(newNickname.equals(this.userNickname)) throw new IllegalArgumentException("이전 닉네임과 다른 닉네임을 입력해주세요");
        this.userNickname = newNickname;
    }

    public void changeUserPassword(String newUserPassword) {
        if(newUserPassword == null || newUserPassword.isBlank()) throw new IllegalArgumentException("비밀번호를 입력해주세요");
        if(newUserPassword.equals(this.userPassword)) throw new IllegalArgumentException("이전 비밀번호과 다른 비밀번호를 입력해주세요");
        this.userPassword = newUserPassword;
    }
}