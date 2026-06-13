package ktb4.community.controller;

import jakarta.servlet.http.HttpServletRequest;
import ktb4.community.dto.request.CommentRequestDto;
import ktb4.community.dto.request.CreatePostRequestDto;
import ktb4.community.dto.request.UpdatePostRequestDto;
import ktb4.community.dto.response.*;
import ktb4.community.entity.PostLikeId;
import ktb4.community.global.code.SuccessCode;
import ktb4.community.service.CommentService;
import ktb4.community.service.PostLikeService;
import ktb4.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 게시물 관련 HTTP 요청을 처리하는 컨트롤러
// /v1/posts 하위 URI를 담당 (게시물, 댓글, 좋아요 포함)
@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostLikeService postLikeService;
    private final CommentService commentService;

    /**
     * 게시물 생성
     * POST /v1/posts
     * 토큰에서 추출한 userId를 작성자로 사용
     *
     * 요청 바디 : 제목(필수), 내용(필수), 이미지(선택)
     * 응답 : 201 Created, 생성된 게시물 정보 반환
     */
    @PostMapping
    public ResponseEntity<ApiResponseDto> create(HttpServletRequest request, @RequestBody CreatePostRequestDto dto) {
        // 토큰에서 추출한 userId로 작성자 식별
        Long userId = (Long) request.getAttribute("userId");
        CreatePostResponseDto newPost = postService.create(userId, dto);
        return ResponseEntity
                .status(SuccessCode.CREATE_POST_SUCCESS.getStatus())
                .body(ApiResponseDto.success(newPost, SuccessCode.CREATE_POST_SUCCESS));
    }

    /**
     * 게시물 목록 조회 (무한 스크롤)
     * GET /v1/posts?offset=0&limit=10
     * 토큰 불필요 (인증 없이 조회 가능)
     *
     * 응답 : 200 OK, 게시물 목록 Slice 반환 (hasNext로 다음 페이지 존재 여부 판단)
     */
    @GetMapping
    public ResponseEntity<ApiResponseDto> getPosts(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        Slice<PostSummaryResponseDto> posts = postService.getPostsWithPaging(offset, limit);
        return ResponseEntity
                .status(SuccessCode.GET_POSTS_SUCCESS.getStatus())
                .body(ApiResponseDto.success(posts, SuccessCode.GET_POSTS_SUCCESS));
    }

    /**
     * 특정 게시물 상세 조회
     * GET /v1/posts/{id}
     * 토큰 불필요 (인증 없이 조회 가능)
     * 댓글 목록은 GET /v1/posts/{id}/comments 별도 API로 조회
     *
     * 응답 : 200 OK, 게시물 상세 정보 반환
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto> get(HttpServletRequest request, @PathVariable("id") Long postId) {
        Long userId = (Long) request.getAttribute("userId");

        return ResponseEntity
                .status(SuccessCode.GET_POST_SUCCESS.getStatus())
                .body(ApiResponseDto.success(postService.getPostDetail(postId, userId), SuccessCode.GET_POST_SUCCESS));
    }

    /**
     * 게시물 수정
     * PATCH /v1/posts/{id}
     * 토큰에서 추출한 userId로 작성자 본인 여부 검증 (Service에서 처리)
     *
     * 요청 바디 : 제목, 내용, 이미지 중 최소 1개 이상
     * 응답 : 200 OK, 수정된 게시물 정보 반환
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseDto> update(HttpServletRequest request, @PathVariable("id") Long postId, @RequestBody UpdatePostRequestDto dto) {
        // 토큰에서 추출한 userId로 작성자 검증
        Long userId = (Long) request.getAttribute("userId");
        UpdatePostResponseDto updatedPost = postService.update(postId, userId, dto.getTitle(), dto.getContent(), dto.getImage());
        return ResponseEntity
                .status(SuccessCode.UPDATE_POST_SUCCESS.getStatus())
                .body(ApiResponseDto.success(updatedPost, SuccessCode.UPDATE_POST_SUCCESS));

    }

    /**
     * 게시물 삭제
     * DELETE /v1/posts/{id}
     * 토큰에서 추출한 userId로 작성자 본인 여부 검증 (Service에서 처리)
     * DB CASCADE로 해당 게시물의 댓글, 좋아요도 함께 삭제됨
     *
     * 응답 : 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(HttpServletRequest request, @PathVariable("id") Long postId) {
        // 토큰에서 추출한 userId로 작성자 검증
        Long userId = (Long) request.getAttribute("userId");
        postService.delete(postId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 게시물 좋아요
     * POST /v1/posts/{id}/likes
     * 이미 좋아요를 누른 경우 409 반환 (Service에서 처리)
     *
     * 응답 : 200 OK, isLiked(true)와 현재 좋아요 수 반환
     */
    @PostMapping("/{id}/likes")
    public ResponseEntity<ApiResponseDto> pressPostLike(HttpServletRequest request, @PathVariable("id") Long postId) {
        // 토큰에서 추출한 userId로 좋아요 누른 사람 식별
        Long userId = (Long) request.getAttribute("userId");
        PostLikeResponseDto dto = postLikeService.pressLike(postId, userId);
        return ResponseEntity.ok(ApiResponseDto.success(dto, SuccessCode.PRESS_LIKE_SUCCESS));
    }

    /**
     * 게시물 좋아요 취소
     * DELETE /v1/posts/{id}/likes
     * 좋아요가 없는 경우 404 반환 (Service에서 처리)
     *
     * 응답 : 200 OK, isLiked(false)와 현재 좋아요 수 반환
     */
    @DeleteMapping("/{id}/likes")
    public ResponseEntity<ApiResponseDto> cancelPostLike(HttpServletRequest request, @PathVariable("id") Long postId) {
        // 토큰에서 추출한 userId로 복합키 생성
        Long userId = (Long) request.getAttribute("userId");
        PostLikeResponseDto dto = postLikeService.cancelLike(new PostLikeId(postId, userId));
        return ResponseEntity
                .status(SuccessCode.UNDO_LIKE_SUCCESS.getStatus())
                .body(ApiResponseDto.success(dto, SuccessCode.UNDO_LIKE_SUCCESS));
    }

    /**
     * 댓글 생성
     * POST /v1/posts/{id}/comments
     * 토큰에서 추출한 userId를 작성자로 사용
     *
     * 요청 바디 : 댓글 내용(필수)
     * 응답 : 201 Created, 생성된 댓글 정보 반환
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<ApiResponseDto> createComment(HttpServletRequest request, @PathVariable("id") Long postId, @RequestBody CommentRequestDto dto) {
        // 토큰에서 추출한 userId로 작성자 식별
        Long userId = (Long) request.getAttribute("userId");
        CommentResponseDto responseDto = commentService.create(postId, userId, dto);
        return ResponseEntity
                .status(SuccessCode.CREATE_COMMENT_SUCCESS.getStatus())
                .body(ApiResponseDto.success(responseDto, SuccessCode.CREATE_COMMENT_SUCCESS));
    }

    /**
     * 댓글 수정
     * PATCH /v1/posts/{id}/comments/{commentId}
     * 토큰에서 추출한 userId로 작성자 본인 여부 검증 (Service에서 처리)
     *
     * 요청 바디 : 변경할 댓글 내용(필수)
     * 응답 : 200 OK, 수정된 댓글 정보 반환
     */
    @PatchMapping("/{id}/comments/{commentId}")
    public ResponseEntity<ApiResponseDto> updateComment(HttpServletRequest request, @PathVariable("commentId") Long commentId, @RequestBody CommentRequestDto dto) {
        // 토큰에서 추출한 userId로 작성자 검증
        Long userId = (Long) request.getAttribute("userId");
        CommentResponseDto responseDto = commentService.update(commentId, userId, dto);
        return ResponseEntity
                .status(SuccessCode.UPDATE_COMMENT_SUCCESS.getStatus())
                .body(ApiResponseDto.success(responseDto, SuccessCode.UPDATE_COMMENT_SUCCESS));
    }

    /**
     * 댓글 삭제
     * DELETE /v1/posts/{id}/comments/{commentId}
     * 토큰에서 추출한 userId로 작성자 본인 여부 검증 (Service에서 처리)
     *
     * 응답 : 204 No Content
     */
    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(HttpServletRequest request, @PathVariable("commentId") Long commentId) {
        // 토큰에서 추출한 userId로 작성자 검증
        Long userId = (Long) request.getAttribute("userId");
        commentService.delete(commentId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 댓글 목록 조회 (무한 스크롤)
     * GET /v1/posts/{id}/comments?offset=0&limit=10
     * 토큰 불필요 (인증 없이 조회 가능)
     *
     * 응답 : 200 OK, 댓글 목록 Slice 반환 (hasNext로 다음 페이지 존재 여부 판단)
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<ApiResponseDto> getComments(
            @PathVariable("id") Long postId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        Slice<CommentResponseDto> comments = commentService.getComments(postId, offset, limit);
        return ResponseEntity
                .status(SuccessCode.GET_COMMENTS_SUCCESS.getStatus())
                .body(ApiResponseDto.success(comments, SuccessCode.GET_COMMENTS_SUCCESS));
    }
}