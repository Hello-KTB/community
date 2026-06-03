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
    private Long id;

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_image")
    private String image;

    @Column(name = "user_createdAt")
    private LocalDateTime createdAt;

    @Column(name = "user_updatedAt")
    private LocalDateTime updatedAt;

    protected User() {}

    public User(String email, String password, String nickname, String image) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("이메일을 입력해주세요");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("비밀번호를 입력해주세요");
        if (nickname == null || nickname.isBlank()) throw new IllegalArgumentException("닉네임을 입력해주세요");
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.image = image;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static User createUser(String email, String password, String nickname, String image) {
        return new User(email, password, nickname, image);
    }

    public void changeNickname(String newNickname) {
        if(newNickname == null || newNickname.isBlank()) throw new IllegalArgumentException("닉네임을 입력해주세요");
        if(newNickname.equals(this.nickname)) throw new IllegalArgumentException("이전 닉네임과 다른 닉네임을 입력해주세요");
        this.nickname = newNickname;
    }

    public void changePassword(String newPassword) {
        if(newPassword == null || newPassword.isBlank()) throw new IllegalArgumentException("비밀번호를 입력해주세요");
        if(newPassword.equals(this.password)) throw new IllegalArgumentException("이전 비밀번호과 다른 비밀번호를 입력해주세요");
        this.password = newPassword;
    }
}