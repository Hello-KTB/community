package ktb4.community.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ktb4.community.entity.Post;
import ktb4.community.entity.QPost;
import ktb4.community.entity.QPostLike;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> searchPosts(String keyword, String sort, int offset, int limit) {
        QPost post = QPost.post;

        return queryFactory
                .selectFrom(post)
                .leftJoin(post.author).fetchJoin() // N+1 방지
                .where(titleOrContentContains(keyword)) // 키워드 조건
                .orderBy(getSortOrder(sort, post))      // 동적 정렬 (최신순, 조회수순, 좋아요순)
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    // 제목 또는 내용 검색 조건 (LIKE '%keyword%')
    private BooleanExpression titleOrContentContains(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null; // 검색어가 없으면 전체 조회
        }

        // 검색어의 모든 공백 제거 (예: "번 더" -> "번더")
        String noSpaceKeyword = keyword.replaceAll("\\s+", "");
        QPost post = QPost.post;

        // DB의 title/content 컬럼 공백을 제거한 값과, 검색어 공백을 제거한 값을 비교
        StringExpression titleNoSpace = Expressions.stringTemplate("replace({0}, ' ', '')", post.title);
        StringExpression contentNoSpace = Expressions.stringTemplate("replace({0}, ' ', '')", post.content);

        return titleNoSpace.containsIgnoreCase(noSpaceKeyword)
                .or(contentNoSpace.containsIgnoreCase(noSpaceKeyword));
    }

    // 동적 정렬 처리
    private OrderSpecifier<?> getSortOrder(String sort, QPost post) {
        if ("views".equalsIgnoreCase(sort)) {
            return post.views.desc(); // 조회수순
        } else if ("likes".equalsIgnoreCase(sort)) {
            // 서브쿼리를 Expressions.asNumber로 감싸고 OrderSpecifier 객체 생성
            QPostLike postLike = QPostLike.postLike;
            var likeCountSubquery = JPAExpressions.select(postLike.count())
                    .from(postLike)
                    .where(postLike.postLikeId.postId.eq(post.id));

            return new OrderSpecifier<>(Order.DESC, Expressions.asNumber(likeCountSubquery));
        }
        return post.createdAt.desc(); // 기본값: 최신순 (recent)
    }
}