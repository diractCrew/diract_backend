package com.diract.global.config;

import com.diract.auth.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Spring Security 설정
 *
 * - JWT 기반 인증을 사용하는 Stateless API 보안 설정
 * - 인증이 필요 없는 엔드포인트와 보호할 엔드포인트 분리
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Security 필터 체인 구성
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // REST API + JWT 기반이므로 CSRF 비활성화
            .csrf(AbstractHttpConfigurer::disable)

            // CorsConfigurationSource Bean(CorsConfig) 적용
            .cors(withDefaults())

            // 요청별 인증/인가 규칙
            .authorizeHttpRequests(auth -> auth
                // 인증 없이 접근 가능한 엔드포인트
                .requestMatchers(
                    "/api/auth/**",
                    "/actuator/health"
                ).permitAll()

                // 그 외 요청은 인증 필요
                .anyRequest().authenticated()
            )

            // 세션을 사용하지 않는 Stateless 설정
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 등록
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
