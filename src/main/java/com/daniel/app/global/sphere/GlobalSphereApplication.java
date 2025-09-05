package com.daniel.app.global.sphere;

import com.daniel.app.global.sphere.models.Role;
import com.daniel.app.global.sphere.models.User;
import com.daniel.app.global.sphere.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
@EnableJpaAuditing( auditorAwareRef = "auditWareImpl")
public class GlobalSphereApplication {

	private  final UserRepository userRepository;
	private  final PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(GlobalSphereApplication .class, args);
	}


	@PostConstruct
	public  void populate(){
		log.info("Populating user in db");
		User admin = new User(
				"Alex Doe",
				"Computer Science • Spring MVC",
				null,
				"Instructor",
				Role.INSTRUCTOR
		);
		admin.setPassword(passwordEncoder.encode("12345"));
		admin.setFollowersCount(0);
		admin.setFollowingCount(0);
		admin.setPostsCount(0);
		admin.setEmail("user@example.com");
		admin.setLinkedInUrl("https://linkedin.com/in/alexdoe");
		admin.setGithubUrl("https://github.com/alexdoe");



		User user = new User(
				"Liberty Mukubvu",
				"Computer Science • Spring MVC",
				null,
				"Student",
				Role.STUDENT
		);
		user.setPassword(passwordEncoder.encode("12345"));
		user.setFollowersCount(0);
		user.setFollowingCount(0);
		user.setPostsCount(0);
		user.setEmail("alex.doe@example.com");
		user.setLinkedInUrl("https://linkedin.com/in/alexdoe");
		user.setGithubUrl("https://github.com/alexdoe");

		userRepository.saveAll(List.of(admin,user));
	}

}
