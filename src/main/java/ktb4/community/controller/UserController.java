package ktb4.community.controller;

import ktb4.community.dto.request.UserRequestDto;
import ktb4.community.dto.response.UserResponseDto;
import ktb4.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/users")
/*
@RestController :
@Controller + @ResponseBody로 RestController의 주용도인 JSON 형태의 객체 데이터를 반환

@RequestMapping :
사용자의 요청을 스프링 Controller 메소드 또는 클래스에 매핑하기 위해 사용,
클래스 레벨에 사용하면 클래스 내 모든 메소드에 공유되는 매핑 주소로 설정
 */
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; // UserService 객체 생성은 위 @RequiredArgsConstructor에 의해 자동으로 됨

    @PostMapping // /v1/users로 들어오는 Post요청 처리
    public UserResponseDto createUser(@RequestBody UserRequestDto request) {
        return userService.createUser(request);
    }

    @GetMapping("/{userId}") // /v1/users/{userId}로 들어오는 Get요청처리
    public UserResponseDto getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }
}