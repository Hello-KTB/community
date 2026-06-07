package ktb4.community.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 댓글 엔티티
@Entity
// 모든 필드의 getter 메서드 자동 생성
@Getter
// 기본 생성자, protected로 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    // 기본키 지정
    // DB가 AUTO_INCREMENT를 통해 PK 자동 생성
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    // 다대일(N:1) 관계로, 여러 댓글이 한 명의 작성자에 속함
    // author 필드에 실제 접근할 때만 User를 DB에서 조회 (지연로딩)
    // comment 테이블의 user_id 컬럼이 외래키로 User를 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    // 다대일(N:1) 관계로, 여러 댓글이 하나의 게시글에 속함
    // post 필드에 실제 접근할 때만 Post를 DB에서 조회 (지연로딩)
    // comment 테이블의 post_id 컬럼이 외래키로 Post를 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // NOT NULL 제약
    @Column(name = "comment_content", nullable = false)
    private String content;

    // NOT NULL 제약
    @Column(name = "comment_createdAt", nullable = false)
    private LocalDateTime createdAt;

    // NOT NULL 제약
    @Column(name = "comment_updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    public Comment(User author, Post post, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.author    = author;
        this.post      = post;
        this.content   = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 댓글 내용 수정 (수정 시각 자동 갱신)
    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
}