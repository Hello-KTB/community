/*
import ktb4.community.dto.request.UserRequestDto;
import ktb4.community.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto createUser(UserRequestDto requestDto) {
        User user = new User(
                requestDto.getUserEmail(),
                requestDto.getUserPassword(),
                requestDto.getUserNickname()
        );
        user.setUserCreatedAt(new Date());
        user.setUserUpdatedAt(new Date());
        SecurityProperties.User savedUser = userRepository.save(user);
        return new UserResponseDto(savedUser);
    }

    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findByUserId(userId);
        return new UserResponseDto(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteByUserId(userId);
    }
}*/