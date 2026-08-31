package com.portfolio.config;

import com.portfolio.security.JwtAuthenticationFilter; import com.portfolio.repository.UserRepository; import org.springframework.context.annotation.*; import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.config.http.SessionCreationPolicy; import org.springframework.security.core.userdetails.*; import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.security.web.SecurityFilterChain; import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; import org.springframework.web.cors.*; import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
    @Bean UserDetailsService userDetailsService(UserRepository repo){return username->repo.findByUsername(username).map(u->User.withUsername(u.getUsername()).password(u.getPassword()).roles(u.getRole().name()).build()).orElseThrow(()->new UsernameNotFoundException("User not found"));}
    @Bean SecurityFilterChain filterChain(HttpSecurity http,JwtAuthenticationFilter jwt)throws Exception{
        http.csrf(c->c.disable()).cors(c->{}).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a->a.requestMatchers("/api/auth/**","/api/images/**","/api/videos/**","/api/categories/**","/uploads/**","/error").permitAll().anyRequest().hasRole("OWNER")).addFilterBefore(jwt,UsernamePasswordAuthenticationFilter.class); return http.build();
    }
    @Bean CorsConfigurationSource cors(){var c=new CorsConfiguration(); c.setAllowedOriginPatterns(List.of("*")); c.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS")); c.setAllowedHeaders(List.of("*")); c.setAllowCredentials(false); var s=new UrlBasedCorsConfigurationSource(); s.registerCorsConfiguration("/**",c); return s;}
}
