package ktb4.community.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

// 복합키 클래스 (post_id + user_id)
// @Embeddable : PostLike 엔티티의 @EmbeddedId로 사용되는 복합키 타입임을 선언
@Embeddable
// 모든 필드의 getter 메서드 자동 생성
@Getter
// equals()와 hashCode() 자동 생성
// JPA는 복합키 동등성 비교에 equals/hashCode를 사용하므로 필수
@EqualsAndHashCode
// 기본 생성자, protected로 생성 (JPA 프록시 생성용)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 모든 필드를 받는 생성자 자동 생성
@AllArgsConstructor
public class PostLikeId {

    // 좋아요가 눌린 게시글의 PK
    @Column(name = "post_id")
    private Long postId;

    // 좋아요를 누른 사용자의 PK
    @Column(name = "user_id")
    private Long userId;
}