package com.diract.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * JWT 토큰 응답 DTO
 *
 * OAuth 로그인 성공 시 클라이언트에게 반환하는 토큰 정보
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {

    // Access Token - API 호출 시 사용 (1시간)
    private String accessToken;

    // Refresh Token - Access Token 재발급 시 사용 (2주)
    private String refreshToken;

    // 토큰 타입 (Bearer)
    @Builder.Default
    private String tokenType = "Bearer";

    // Access Token 만료 시간 (초 단위)
    // 클라이언트가 만료 시점을 알 수 있도록 제공
    private Long expiresIn;
}