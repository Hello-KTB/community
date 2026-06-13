package ktb4.community.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 게시글 엔티티
@Entity
// 모든 필드의 getter 메서드 자동 생성
@Getter
// 기본 생성자, protected로 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    // 기본키 지정
    // DB가 AUTO_INCREMENT를 통해 PK 자동 생성
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    // 다대일(N:1) 관계로, 여러 게시글이 한 명의 작성자에 속함
    // author 필드에 실제 접근할 때만 User를 DB에서 조회 (지연로딩)
    // post 테이블의 user_id 컬럼이 외래키로 User를 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    // NOT NULL 제약
    @Column(name = "post_name", nullable = false)
    private String title;

    // NOT NULL 제약
    @Column(name = "post_content", nullable = false)
    private String content;

    // 이미지는 선택 사항 (null 허용)
    @Column(name = "post_image")
    private String image;

    // NOT NULL 제약
    @Column(name = "post_createdAt", nullable = false)
    private LocalDateTime createdAt;

    // NOT NULL 제약
    @Column(name = "post_updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    // NOT NULL 제약
    @Column(name = "post_views", nullable = false)
    private int views;

    public Post(User author, String title, String content, String image,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.author    = author;
        this.title     = title;
        this.content   = content;
        this.image     = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.views     = 0;  // 조회수는 생성 시 0으로 초기화
    }

    // 게시글 내용 수정 (수정 시각 자동 갱신)
    public void update(String title, String content, String image) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (image != null) this.image = image;
        this.updatedAt = LocalDateTime.now();
    }

    // 조회수 1 증가
    public void increaseViews() {
        this.views++;
    }
}