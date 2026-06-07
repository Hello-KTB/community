package ktb4.community.entity;

import jakarta.persistence.*;
import lombok.*;

// 게시글 좋아요 엔티티
// 복합키(PostLikeId)를 사용해 user_id + post_id 조합으로 중복 좋아요를 방지
@Entity
// 모든 필드의 getter 메서드 자동 생성
@Getter
// 모든 필드를 받는 생성자 자동 생성
@AllArgsConstructor
public class PostLike {

    // 복합키를 @EmbeddedId로 지정
    // PostLikeId(post_id + user_id) 조합이 이 테이블의 PK
    @EmbeddedId
    private PostLikeId postLikeId;

    // JPA 프록시 생성용 기본 생성자 (protected로 외부 직접 생성 차단)
    protected PostLike() {}
}