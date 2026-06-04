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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        if (userRepository.count() >= 100 && postRepository.count() >= 100) return;

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        IntStream.rangeClosed(1, 100).forEach(i -> {
            String rawPassword = "123aS!" + i;
            String encodedPassword = passwordEncoder.encode(rawPassword);
            User user = new User("tester"+i+"@adapterz.kr", encodedPassword, "tester"+i, "profile_image"+i);
            userRepository.save(user);

            Post post = new Post(user,"title"+i, "content"+i, "content_image"+i, LocalDateTime.now(), LocalDateTime.now());
            postRepository.save(post);
        });
    }
}