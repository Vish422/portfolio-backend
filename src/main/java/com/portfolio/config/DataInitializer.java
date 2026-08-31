package com.portfolio.config;

import com.portfolio.entity.*;
import com.portfolio.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seed(
            UserRepository users,
            CategoryRepository cats,
            PasswordEncoder encoder) {

        return args -> {

            User u = users.findByUsername("owner")
                    .orElseGet(User::new);

            u.setUsername("owner");
            u.setEmail("owner@example.com");
            u.setPassword(encoder.encode("ChangeMe123!"));
            u.setRole(Role.OWNER);

            users.save(u);

            if (cats.count() == 0) {
                for (String n : new String[]{
                        "Projects",
                        "Certificates",
                        "College",
                        "Coding",
                        "Travel",
                        "Other"
                }) {
                    Category c = new Category();
                    c.setName(n);
                    cats.save(c);
                }
            }
        };
    }
}