package ktb4.community.service;

import ktb4.community.dto.request.CreatePostRequestDto;
import ktb4.community.entity.Post;
import ktb4.community.entity.User;
import ktb4.community.filter.JwtAuthFilter;
import ktb4.community.repository.PostRepository;
import ktb4.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다"));
    }

    @Transactional
    public Post update(Long id, String title, String content, String image) {
        Post post = findById(id);

        post.setTitle(title);
        post.setContent(content);
        post.setImage(image);
        return post;
    }

    @Transactional
    public void delete(Long id) {
        postRepository.delete(findById(id));
    }
}