package ktb4.community.service;

import ktb4.community.dto.request.UserRequestDto;
import ktb4.community.dto.response.UserResponseDto;
import ktb4.community.entity.User;
import ktb4.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
        User savedUser = userRepository.save(user);
        return new UserResponseDto(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findByUserId(userId);
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateNickname(Long userId, UserRequestDto request) {
        User user = userRepository.findByUserId(userId);
        user.changeNickname(request.getUserNickname());
        return new UserResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteByUserId(userId);
    }
}
