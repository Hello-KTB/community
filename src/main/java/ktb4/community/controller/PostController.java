package ktb4.community.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ktb4.community.dto.request.CreatePostRequestDto;
import ktb4.community.dto.request.UpdatePostRequestDto;
import ktb4.community.dto.response.ApiResponseDto;
import ktb4.community.dto.response.CreatePostResponseDto;
import ktb4.community.dto.response.UpdatePostResponseDto;
import ktb4.community.entity.Post;
import ktb4.community.filter.JwtAuthFilter;
import ktb4.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponseDto> create(HttpServletRequest request, @RequestBody CreatePostRequestDto dto) {
        Long id = (Long)request.getAttribute("userId");
        Post post = postService.create(id, dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>(
                        HttpStatus.CREATED.value(),
                        true,
                        "게시물이 등록되었습니다",
                        new CreatePostResponseDto(post.getId(), post.getTitle(), post.getAuthor().getNickname(), post.getContent(), post.getImage(), post.getCreatedAt(), post.getUpdatedAt())
                        )
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto> get(HttpServletRequest request, @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>(
                                HttpStatus.CREATED.value(),
                                true,
                                "게시물이 등록되었습니다",
                                new CreatePostResponseDto(post.getId(), post.getTitle(), post.getAuthor().getNickname(), post.getContent(), post.getImage(), post.getCreatedAt(), post.getUpdatedAt())
                        )
                );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseDto> update(HttpServletRequest request, @RequestBody UpdatePostRequestDto dto) {
        Long id = (Long)request.getAttribute("userId");
        Post updatedPost = postService.update(id, dto.getTitle(),dto.getContent(), dto.getImage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponseDto<>(
                        HttpStatus.OK.value(),
                        true,
                        "게시물이 수정되었습니다",
                        new UpdatePostResponseDto(updatedPost.getId(), updatedPost.getTitle(), updatedPost.getContent(), updatedPost.getImage(), updatedPost.getUpdatedAt())
                ));
    }

    @DeleteMapping("/{id}")
    public void delete(HttpServletRequest request) {
        Long id = (Long)request.getAttribute("userId");
        postService.delete(id);
    }
}