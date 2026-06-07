package ktb4.community.service;

import ktb4.community.dto.request.CommentRequestDto;
import ktb4.community.dto.response.CommentResponseDto;
import ktb4.community.entity.Comment;
import ktb4.community.entity.Post;
import ktb4.community.entity.User;
import ktb4.community.repository.CommentRepository;
import ktb4.community.repository.PostRepository;
import ktb4.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDto create(Long postId, Long userId, CommentRequestDto request) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다"));
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        Comment comment = new Comment(author, post, request.getContent(), createdAt, updatedAt);

        commentRepository.save(comment);
        return new CommentResponseDto(comment.getId(), comment.getAuthor().getNickname(), comment.getContent(), comment.getCreatedAt(), comment.getUpdatedAt());
    }

    @Transactional(readOnly = true)
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다"));
    }

    @Transactional
    public CommentResponseDto update(Long id, Long userId, CommentRequestDto request) {
        Comment comment = findById(id);
        if(!comment.getAuthor().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 댓글의 게시자가 아닙니다");
        }
        comment.setContent(request.getContent());

        return new CommentResponseDto(comment.getId(), comment.getAuthor().getNickname(), comment.getContent(), comment.getCreatedAt(), comment.getUpdatedAt());
    }

    @Transactional
    public void delete(Long id, Long userId) {
        Comment comment = findById(id);
        if(!comment.getAuthor().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 댓글의 게시자가 아닙니다");
        }
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public Slice<CommentResponseDto> getComments(Long postId, int page, int size) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다"));
        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.findByPostOrderByCreatedAtDesc(post, pageable)
                .map(comment -> new CommentResponseDto(
                        comment.getId(),
                        comment.getAuthor().getNickname(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getUpdatedAt()
                ));
    }
}
