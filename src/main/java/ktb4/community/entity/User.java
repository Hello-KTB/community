package ktb4.community.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// JPA가 관리하는 엔티티 클래스임을 선언
@Entity
// 매핑할 DB 이름 명시 (없을 시에는 클래스명이 기본값)
@Table(name = "user")
// 필드의 getter, setter 메서드 자동 생성
@Getter
// 기본 생성자를 protected로 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)// 모든 필드의 getter 메서드 자동 생성
public class User {
    // 기본키 지정
    // DB가 AUTO_INCREMENT를 통해 PK 자동 생성
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    // NOT NULL + UNIQUE 제약
    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    // NOT NULL + UNIQUE 제약
    @Column(name = "user_nickname", nullable = false, unique = true)
    private String nickname;

    // NOT NULL 제약
    @Column(name = "user_password", nullable = false)
    private String password;

    // 프로필 이미지는 선택 가능 (null값 가능)
    @Column(name = "user_image")
    private String profileImage;

    // 생성 시점
    @Column(name = "user_createdAt")
    private LocalDateTime createdAt;

    // 수정 시간
    @Column(name = "user_updatedAt")
    private LocalDateTime updatedAt;

    /**
     * 실제 사용 생성자
     * 필수값(이메일, 비번, 닉네임)에 대한 유효성 검사
     */
    public User(String email, String password, String nickname, String profileImage) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 닉네임 변경, 변경 시 업데이트 시간도 갱신
    public void updateNickname(String nickname) {
        this.nickname = nickname;
        this.updatedAt = LocalDateTime.now();
    }

    // 프로필 이미지 변경, 변경 시 업데이트 시간도 갱신
    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
        this.updatedAt = LocalDateTime.now();
    }

    // 비밀번호 변경, 변경 시 업데이트 시간도 갱신
    public void updatePassword(String password) {
        this.password = password;
        this.updatedAt = LocalDateTime.now();
    }
}