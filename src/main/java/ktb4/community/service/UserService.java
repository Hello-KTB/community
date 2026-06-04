package ktb4.community.service;

import ktb4.community.dto.request.CreateUserRequestDto;
import ktb4.community.entity.User;
import ktb4.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User create(CreateUserRequestDto createUserRequestDto) {
        String encodedPassword = passwordEncoder.encode(createUserRequestDto.getPassword());

        User user = new User(createUserRequestDto.getEmail(), encodedPassword, createUserRequestDto.getNickname(), createUserRequestDto.getProfileImage());
        return userRepository.save(user);
    }


    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));
    }

    /*
    public User getReferenceById(Long id) {
        return userRepository.getReferenceById(id);
    }*/

    @Transactional
    public User update(Long id, String nickname, String profileImage) {
        User user = findById(id);
        if (nickname != null) {
            user.changeNickname(nickname);
        }
        if (profileImage != null) {
            user.changeProfileImage(profileImage);
        }
        return userRepository.save(user);
    }

    @Transactional
    public User updatePassword(Long id, String password, String validatePassword) {
        User user = findById(id);
        if (password != null && validatePassword != null && password.equals(validatePassword)) {
            user.changePassword(passwordEncoder.encode(password));
        }
        return userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        // 프록시 반환 (접근 시 초기화)
        userRepository.delete(findById(id));
    }

    /*
    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }*/
}