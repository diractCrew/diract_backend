package com.diract.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * 브라우저/웹뷰 기반 클라이언트의 Cross-Origin 호출을 허용하기 위한 CORS 설정
 */
@Configuration
public class CorsConfig {

    /**
     * Spring Security에서 사용할 CORS 설정
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 로컬 개발 환경에서 허용할 Origin (웹/웹뷰 테스트용)
        configuration.setAllowedOrigins(List.of(
            "http://localhost:3000",
            "http://localhost:8080",
            "http://127.0.0.1:3000",
            "http://127.0.0.1:8080"
        ));

        // 허용할 HTTP 메서드
        configuration.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        // 요청 헤더 허용 (Authorization, Content-Type 등)
        configuration.setAllowedHeaders(List.of("*"));

        // 클라이언트에서 접근 가능한 응답 헤더
        configuration.setExposedHeaders(List.of("Authorization"));

        // Authorization 헤더 등 자격 증명 포함 허용
        configuration.setAllowCredentials(true);

        // Preflight 요청 결과 캐시 시간
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
