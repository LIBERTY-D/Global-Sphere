package com.daniel.app.global.sphere.config;

import com.daniel.app.global.sphere.models.User;
import com.daniel.app.global.sphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading Custom User Details");
        Optional<User> user=  userRepository.findByEmail(username);
        if(user.isEmpty()){
            throw  new UsernameNotFoundException("Email not found");
        }
        return user.get();
    }
}
