package com.diract.auth.service;

import com.diract.auth.domain.RefreshToken;
import com.diract.auth.dto.JwtToken;
import com.diract.auth.repository.RefreshTokenRepository;
import com.diract.auth.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * JWT 토큰 발급/재발급 서비스
 *
 * - Access / Refresh 토큰 생성
 * - Refresh Token 저장 및 검증
 * - 로그아웃 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 로그인 성공 시 Access / Refresh 토큰 발급
     * - Refresh Token은 서버 저장소에 저장
     */
    public JwtToken generateTokens(String userId) {
        String accessToken = jwtTokenProvider.generateAccessToken(userId);
        String refreshTokenValue = jwtTokenProvider.generateRefreshToken(userId);

        long expiresAtMillis =
            System.currentTimeMillis() + jwtTokenProvider.getRefreshTokenExpirationMillis();

        RefreshToken refreshToken =
            RefreshToken.create(userId, refreshTokenValue, expiresAtMillis);

        refreshTokenRepository.save(refreshToken);

        return JwtToken.builder()
            .accessToken(accessToken)
            .refreshToken(refreshTokenValue)
            .expiresIn(jwtTokenProvider.getAccessTokenExpirationInSeconds())
            .build();
    }

    /**
     * Refresh Token을 이용한 Access Token 재발급
     */
    public JwtToken refreshAccessToken(String refreshTokenValue) {
        RefreshToken stored = refreshTokenRepository.findByToken(refreshTokenValue)
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Refresh Token"));

        if (stored.isExpired()) {
            refreshTokenRepository.delete(refreshTokenValue);
            throw new IllegalArgumentException("만료된 Refresh Token");
        }

        // Refresh Token JWT의 subject(userId) 기준으로 사용자 식별
        String userId = jwtTokenProvider.getUserIdFromToken(refreshTokenValue);

        // 저장된 사용자 정보와 불일치 시 토큰 무효 처리
        if (stored.getUserId() != null && !stored.getUserId().equals(userId)) {
            refreshTokenRepository.delete(refreshTokenValue);
            throw new IllegalArgumentException("Refresh Token 사용자 정보 불일치");
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);

        return JwtToken.builder()
            .accessToken(newAccessToken)
            .refreshToken(refreshTokenValue) // Refresh Token은 유지 (회전 정책 미적용)
            .expiresIn(jwtTokenProvider.getAccessTokenExpirationInSeconds())
            .build();
    }

    /**
     * 로그아웃 처리
     * - 현재 세션의 Refresh Token 삭제
     */
    public void logout(String refreshTokenValue) {
        refreshTokenRepository.delete(refreshTokenValue);
    }
}
