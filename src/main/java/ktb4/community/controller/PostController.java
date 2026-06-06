package ktb4.community.controller;

import jakarta.servlet.http.HttpServletRequest;
import ktb4.community.dto.request.CreatePostRequestDto;
import ktb4.community.dto.request.UpdatePostRequestDto;
import ktb4.community.dto.response.*;
import ktb4.community.entity.Post;
import ktb4.community.repository.PostRepository;
import ktb4.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

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

    @GetMapping
    public ResponseEntity<ApiResponseDto> getPosts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10")int size) {

        Page<PostSummaryResponseDto> posts = postService.getPostsWithPaging(page,size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponseDto<>(
                                HttpStatus.OK.value(),
                                true,
                                "게시물 목록을 조회합니다",
                                posts
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto> get(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponseDto<>(
                                HttpStatus.OK.value(),
                                true,
                                "특정 게시물을 조회합니다",
                                postService.getPostDetail(id)
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