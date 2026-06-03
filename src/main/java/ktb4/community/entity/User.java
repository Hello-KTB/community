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
}