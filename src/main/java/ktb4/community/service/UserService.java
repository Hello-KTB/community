package ktb4.community.service;

import ktb4.community.entity.User;
import ktb4.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User create(String email, String password, String nickname, String image) {
        User user = new User(email, password, nickname, image);
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));
    }

    public User getReferenceById(Long id) {
        return userRepository.getReferenceById(id);
    }

    @Transactional
    public User update(Long id, String nickname) {
        User user = findById(id);
        if (nickname != null) {
            user.changeNickname(nickname);
        }
        return user;
    }

    @Transactional
    public void delete(Long id) {
        // 프록시 반환 (접근 시 초기화)
        userRepository.delete(findById(id));
    }

    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}