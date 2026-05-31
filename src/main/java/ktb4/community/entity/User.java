package ktb4.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
/*
@Data :
롬복에서 제공하는 어노테이션으로, @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor를 모두 자동으로 적용한다.

@NoArgsConstructor :
파라미터가 없는 기본 생성자 생성하지만 final이나 @NonNull로 선언된 필드가 있을 경우에는 무조건 초기화가 되어야 하므로,
@RequiredArgsConstructor가 적용되어, 해당 어노테이션은 실행 안된다.
즉, 기본 생성자는 직접 작성해야 한다
 */
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자 생성
public class User {
    private Long userId;
    @NonNull
    private String userEmail;
    @NonNull
    private String userPassword;
    @NonNull
    private String userNickname;
    private String userImage;
    private LocalDateTime userCreatedAt;
    private LocalDateTime userUpdatedAt;

    // 기본 생성자
    public User() {}

    // 프로필 이미지 있는 생성자
    public User(String userEmail, String userPassword, String userNickname, String userImage) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userNickname = userNickname;
        this.userImage = userImage;
    }

    // 비밀번호 변경 메서드
    public void changePassword(String userPassword) {
        this.userPassword = userPassword;
        updateUser(userUpdatedAt);
    }

    // 닉네임 변경 메서드
    public void changeNickname(String userNickname) {
        this.userNickname = userNickname;
        updateUser(userUpdatedAt);
    }

    // 프로필 이미지 변경 메서드
    public void changeImage(String userImage) {
        this.userImage = userImage;
        updateUser(userUpdatedAt);
    }

    // 수정 시간 업데이트 메서드
    public void updateUser(LocalDateTime userUpdatedAt) {
        this.userUpdatedAt = userUpdatedAt;
    }
}