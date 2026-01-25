package com.diract.auth.service;

import com.diract.auth.dto.GoogleUserInfo;
import com.diract.auth.dto.JwtToken;
import com.diract.domain.user.entity.User;
import com.diract.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * Google OAuth 로그인 처리
 * - 클라이언트가 전달한 Google ID Token(JWT)을 디코딩해 사용자 정보를 추출
 * - 사용자 조회/생성 후 우리 서비스 JWT(Access/Refresh) 발급
 *
 * 주의:
 * - 현재는 서명 검증 없이 payload 디코딩만 수행
 * - 운영 환경에서는 Google 공개키 기반 서명 검증 및 aud/iss/exp 검증 필요
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final TokenService tokenService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Value("${oauth.google.client-id}")
    private String clientId;

    /** Google ID Token 기반 로그인 */
    public JwtToken login(String idToken) {
        log.info("Google OAuth 로그인 시작");

        GoogleUserInfo googleUserInfo = verifyAndDecodeIdToken(idToken);

        User user = userService.getOrCreateUser(
            googleUserInfo.getEmail(),
            googleUserInfo.getName(),
            "GOOGLE"
        );

        JwtToken jwtToken = tokenService.generateTokens(user.getUserId());

        log.info("Google OAuth 로그인 완료: userId={}, email={}", user.getUserId(), user.getEmail());
        return jwtToken;
    }

    /**
     * ID Token 디코딩 및 최소 검증
     * - JWT(payload)만 디코딩하여 사용자 정보 추출
     * - 형식/필수 필드 정도만 확인
     */
    private GoogleUserInfo verifyAndDecodeIdToken(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("유효하지 않은 ID Token 형식");
            }

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            GoogleUserInfo userInfo = objectMapper.readValue(payload, GoogleUserInfo.class);

            if (userInfo.getId() == null || userInfo.getEmail() == null) {
                throw new IllegalArgumentException("ID Token에 필수 정보 없음");
            }

            log.debug("Google ID Token 디코딩 성공: email={}", userInfo.getEmail());
            return userInfo;

        } catch (Exception e) {
            log.error("Google ID Token 처리 실패", e);
            throw new RuntimeException("Google ID Token 처리 중 오류 발생", e);
        }
    }

    /**
     * TODO(운영): Google 공개키로 서명 검증 + iss/aud/exp 검증
     * - https://www.googleapis.com/oauth2/v3/certs
     */
    @SuppressWarnings("unused")
    private boolean verifyIdTokenSignature(String idToken) {
        log.warn("Google ID Token 서명 검증 미구현 (운영 배포 전 구현 필요)");
        return true;
    }
}
