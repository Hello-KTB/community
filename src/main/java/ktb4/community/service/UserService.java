package ktb4.community.service;

import ktb4.community.dto.request.CreateUserRequestDto;
import ktb4.community.dto.request.UpdatePasswordRequestDto;
import ktb4.community.dto.request.UpdateUserRequestDto;
import ktb4.community.dto.response.UpdateUserResponseDto;
import ktb4.community.entity.User;
import ktb4.community.global.code.ErrorCode;
import ktb4.community.global.exception.CustomException;
import ktb4.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 회원 관련 비즈니스 로직을 처리하는 서비스 계층
@Service
@RequiredArgsConstructor
// 기본적으로 읽기 전용 트랜잭션 적용 (SELECT 최적화)
// 데이터 변경이 필요한 메서드에는 개별적으로 @Transactional 적용
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * 비밀번호를 BCrypt로 암호화한 후 User 엔티티 생성 및 저장
     * new User()로 만든 객체는 비영속 상태이므로 save()를 명시적으로 호출해야 INSERT가 됨
     *
     * @param dto 이메일, 비밀번호, 닉네임, 프로필 이미지를 담은 요청 DTO
     * @return 저장된 User 엔티티
     */
    @Transactional
    public void create(CreateUserRequestDto dto) {
        checkEmail(dto.getEmail()); // 이메일 중복 체크
        checkNickname(dto.getNickname()); // 닉네임 중복 체크
        // 비밀번호를 BCrypt로 암호화 (평문 저장 방지)
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = new User(
                dto.getEmail(),
                encodedPassword,
                dto.getNickname(),
                dto.getProfileImage()
        );
        // 비영속 상태 객체이므로 save() 호출 필요
        userRepository.save(user);
    }

    /**
     * ID로 회원 조회
     * 존재하지 않는 ID 요청 시 예외 발생
     *
     * 파라미터 : id 조회할 회원 ID
     * 반환 :  조회된 User 엔티티
     * @throws CustomException
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 회원 정보 수정 (닉네임, 프로필 이미지)
     * null이 아닌 값만 업데이트 (선택적 수정 지원)
     * findById()로 가져온 객체는 영속 상태이므로 @Transactional 종료 시 변경 감지로 자동 UPDATE
     *
     * 파라미터 : id           수정할 회원 ID
     * 파라미터 : nickname     변경할 닉네임 (null이면 변경하지 않음)
     * 파라미터 : profileImage 변경할 프로필 이미지 (null이면 변경하지 않음)
     * 반환 : UpdateUserResponseDto
     */
    @Transactional
    public UpdateUserResponseDto update(Long id, UpdateUserRequestDto dto) {
        User user = findById(id);

        // null이 아닌 값만 선택적으로 업데이트
        if (dto.getNickname() != null) {
            // 현재 닉네임과 희망 닉네임이 다를 경우에 중복 체크 후 업데이트
            if(!dto.getNickname().equals(user.getNickname())) {
                checkNickname(dto.getNickname()); // 닉네임 중복 체크
            }
            user.updateNickname(dto.getNickname());
        }
        if (dto.getProfileImage() != null) user.updateProfileImage(dto.getProfileImage());
        userRepository.save(user);

        return new UpdateUserResponseDto(user.getNickname(), user.getProfileImage());
    }

    /**
     * 비밀번호 변경
     * 새 비밀번호와 확인 비밀번호가 일치할 때만 변경
     * 변경 전 BCrypt로 암호화하여 저장
     *
     * 파라미터 : id               변경할 회원 ID
     * 파라미터 : password         새 비밀번호
     */
    @Transactional
    public void updatePassword(Long id, UpdatePasswordRequestDto dto) {
        User user = findById(id);
        // 새 비밀번호가 현재 비밀번호와 같으면 변경 불가
        if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.SAME_AS_CURRENT_PASSWORD);
        }
        user.updatePassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
    }

    /**
     * 회원 탈퇴
     * ID로 회원을 조회한 후 삭제
     * DB에 ON DELETE CASCADE가 설정되어 있으므로 해당 회원의 게시글, 댓글, 좋아요도 함께 삭제됨
     *
     * 파라미터 : id 삭제할 회원 ID
     */
    @Transactional
    public void delete(Long id) {
        userRepository.delete(findById(id));
    }

    /**
     * 이메일 중복 체크
     * 이미 존재하는 이메일이면 409 Conflict 예외 발생
     * 회원가입 시 이메일 입력 단계에서 실시간으로 호출됨
     *
     * 파라미터 : email 중복 체크할 이메일
     * @throws CustomException
     */
    public void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL);
        }
    }

    /**
     * 닉네임 중복 체크
     * 이미 존재하는 닉네임이면 409 Conflict 예외 발생
     * 회원가입 시 닉네임 입력 단계에서 실시간으로 호출됨
     *
     * 파라미터 : nickname 중복 체크할 닉네임
     * @throws CustomException
     */
    public void checkNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_NICKNAME);
        }
    }
}