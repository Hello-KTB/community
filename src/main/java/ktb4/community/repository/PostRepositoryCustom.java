package ktb4.community.repository;

import ktb4.community.entity.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> searchPosts(String keyword, String sort, int offset, int limit);
}
