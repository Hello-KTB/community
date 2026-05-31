package ktb4.community.repository;

import ktb4.community.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public User save(User user) {
        String sql = "INSERT INTO USER(user_email, user_nickname, user_password, user_image, user_createdAt, user_updatedAt) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                user.getUserEmail(),
                user.getUserNickname(),
                user.getUserPassword(),
                user.getUserImage(),
                user.getUserCreatedAt(),
                user.getUserUpdatedAt());

        return user;
    }

    public User findByUserId(Long userId) {
        String sql = "SELECT * FROM USER WHERE USER_ID = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            User user = new User();
            user.setUserId(rs.getLong("user_id"));
            user.setUserNickname(rs.getString("user_nickname"));
            user.setUserEmail(rs.getString("user_email"));
            return user;
        }, userId);
    }

    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM USER WHERE USER_ID = ?";
        jdbcTemplate.update(sql, userId);
    }


}