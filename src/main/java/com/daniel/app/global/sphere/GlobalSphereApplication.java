package com.daniel.app.global.sphere;

import com.daniel.app.global.sphere.models.Role;
import com.daniel.app.global.sphere.models.User;
import com.daniel.app.global.sphere.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditWareImpl")
public class GlobalSphereApplication {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment env;

    public static void main(String[] args) {
        SpringApplication.run(GlobalSphereApplication.class, args);
    }


    @PostConstruct
    @Profile("dev")
    public void populate() {
        if (!Boolean.parseBoolean(env.getProperty("custom.populate_db", "false"))) {
            return;
        }

        log.info("POPULATING EXAMPLE USERS");

        User admin = new User(
                "User",
                "Computer Science • Spring MVC",
                null,
                "Instructor",
                Role.INSTRUCTOR
        );
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setFollowersCount(0);
        admin.setFollowingCount(0);
        admin.setPostsCount(0);
        admin.setEmail("user@example.com");
        admin.setLinkedInUrl("https://linkedin.com/in/alexdoe");
        admin.setGithubUrl("https://github.com/alexdoe");

        User user = new User(
                "alex doe",
                "Computer Science • Spring MVC",
                null,
                "Student",
                Role.STUDENT
        );
        user.setPassword(passwordEncoder.encode("123456"));
        user.setFollowersCount(0);
        user.setFollowingCount(0);
        user.setPostsCount(0);
        user.setEmail("alex.doe@example.com");
        user.setLinkedInUrl("https://linkedin.com/in/alexdoe");
        user.setGithubUrl("https://github.com/alexdoe");

        User admin2 = new User(
                "Danie MK",
                "Computer Science",
                null,
                "Instructor",
                Role.ADMIN
        );
        admin2.setPassword(passwordEncoder.encode("123456"));
        admin2.setFollowersCount(0);
        admin2.setFollowingCount(0);
        admin2.setPostsCount(0);
        admin2.setEmail("daniel@gmail.com");

        userRepository.saveAll(List.of(admin, user, admin2));
    }

}
