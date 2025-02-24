package com.jungook.zerotodeploy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // 세션 활성화 (STATELESS에서 변경)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/signup.html", "/api/auth/signup", "/login.html", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/api/**").authenticated()  // API 경로는 인증 필요
                .anyRequest().permitAll()  // 그 외 모든 요청은 허용
            )
            .formLogin(form -> form
                .loginPage("/login.html")  // 로그인 페이지 설정
                .defaultSuccessUrl("/", true)  // 로그인 성공 후 리디렉트할 페이지
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html")
                .permitAll()
            );

        return http.build();
    }
}
