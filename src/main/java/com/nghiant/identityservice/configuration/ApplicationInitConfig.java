package com.nghiant.identityservice.configuration;

import com.nghiant.identityservice.entity.User;
import com.nghiant.identityservice.enums.Role;
import com.nghiant.identityservice.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {

  private final PasswordEncoder passwordEncoder;

  @Bean
  ApplicationRunner applicationRunner(UserRepository userRepository) {
    return args -> {
      if (userRepository.findUserByUsername("admin").isEmpty()) {
        Set<String> roles = new HashSet<>();
        roles.add(Role.ADMIN.name());

        User user = User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin"))
            .roles(roles)
            .build();

        userRepository.save(user);

        log.warn("admin account has been created with default password: admin");
      }
    };
  }
}
