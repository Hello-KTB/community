package ktb4.community.service;

import ktb4.community.dto.request.CreatePostRequestDto;
import ktb4.community.dto.response.PostDetailResponseDto;
import ktb4.community.dto.response.PostSummaryResponseDto;
import ktb4.community.entity.Post;
import ktb4.community.entity.User;
import ktb4.community.repository.PostRepository;
import ktb4.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Post create(Long userId, CreatePostRequestDto request) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));

        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        Post post = new Post(author, request.getTitle(), request.getContent(), request.getImage(), createdAt, updatedAt);

        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다"));
    }

    @Transactional(readOnly = true)
    public Page<PostSummaryResponseDto> getPostsWithPaging(int page, int size) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));
        Page<Post> posts = postRepository.findAll(pageable);

        return posts.map(post -> new PostSummaryResponseDto(
                post.getId(),
                post.getTitle(),
                post.getAuthor().getNickname(),
                0,  // likes
                0,  // comments
                post.getViews(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        ));
    }

    @Transactional(readOnly = true)
    public PostDetailResponseDto getPostDetail(Long id) {
        Post post = findById(id);

        return new PostDetailResponseDto(
                post.getId(),
                post.getTitle(),
                post.getAuthor().getNickname(),
                post.getContent(),
                post.getImage(),
                0,
                0,
                post.getViews(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                List.of()
        );
    }

    @Transactional
    public Post update(Long id, Long userId, String title, String content, String image) {
        Post post = findById(id);
        if(!post.getAuthor().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 게시물의 게시자가 아닙니다");
        }
        post.setTitle(title);
        post.setContent(content);
        post.setImage(image);
        return post;
    }

    @Transactional
    public void delete(Long id, Long userId) {
        Post post = findById(id);
        if(!post.getAuthor().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 게시물의 게시자가 아닙니다");
        }
        postRepository.delete(post);
    }
}