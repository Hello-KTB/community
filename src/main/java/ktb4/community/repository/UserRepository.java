package ktb4.community.repository;

import ktb4.community.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일을 통한 회원조회 - 메서드 이름만으로 쿼리 자동 생성
    Optional<User> findByEmail(String email);

    // 특정 이메일이 존재하는지 확인
    boolean existsByEmail(String email);

    // 특정 닉네임이 존재하는지 확인
    boolean existsByNickname(String nickname);
}