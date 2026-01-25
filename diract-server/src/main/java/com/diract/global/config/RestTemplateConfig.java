package com.diract.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 외부 OAuth 서버(Google, Apple) 호출을 위한 HTTP 클라이언트 설정
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 외부 API 호출에 사용할 RestTemplate Bean
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
