package ktb4.community.config;

import ktb4.community.entity.Post;
import ktb4.community.entity.User;
import ktb4.community.repository.PostRepository;
import ktb4.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class SeedConfig {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Bean
    ApplicationRunner seedRunner() {
        return args -> seed(); // 부트 기동 후 1회 실행
    }

    @Transactional
    void seed() {
        if (userRepository.count() >= 10 && postRepository.count() >= 10) return;

        IntStream.rangeClosed(1, 10).forEach(i -> {
            User user = new User("tester"+i+"@adapterz.kr", "123aS!"+i, "tester"+i, "profile_image"+i);
            userRepository.save(user);

            Post post = new Post(user,"title"+i, "content"+i, "content_image"+i, LocalDateTime.now(), LocalDateTime.now());
            postRepository.save(post);
        });
    }
}