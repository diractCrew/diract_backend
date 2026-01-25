package com.diract.auth.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Refresh Token 도메인
 *
 * - 사용자별 로그인 세션을 식별하기 위한 토큰
 * - Access Token 재발급에 사용
 * - 만료 시간 기준으로 유효성 판단
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshToken {

    /** 사용자 ID */
    private String userId;

    /** Refresh Token 값 */
    private String token;

    /** 만료 시각 (epoch millis) */
    private long expiresAt;

    /**
     * Refresh Token 생성
     *
     * @param userId 사용자 ID
     * @param token Refresh Token 값
     * @param expiresAt 만료 시각(epoch millis)
     */
    public static RefreshToken create(String userId, String token, long expiresAt) {
        return new RefreshToken(userId, token, expiresAt);
    }

    /**
     * 만료 여부 확인
     *
     * @return 만료되었으면 true
     */
    public boolean isExpired() {
        return System.currentTimeMillis() > expiresAt;
    }

    /**
     * 남은 TTL(ms)
     *
     * @return 남은 시간(ms)
     */
    public long getTtlMillis() {
        return Math.max(expiresAt - System.currentTimeMillis(), 0);
    }
}
