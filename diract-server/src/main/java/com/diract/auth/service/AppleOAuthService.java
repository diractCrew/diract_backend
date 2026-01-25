package com.diract.auth.service;

import com.diract.auth.dto.AppleUserInfo;
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
 * Apple OAuth 로그인 처리
 * - Apple Identity Token(JWT) payload 디코딩으로 사용자 정보 추출
 * - 사용자 조회/생성 후 우리 서비스 JWT(Access/Refresh) 발급
 *
 * TODO(운영): Apple 공개키로 서명 검증 + iss/aud/exp 검증 필요
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppleOAuthService {

    private final TokenService tokenService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Value("${oauth.apple.client-id}")
    private String clientId;

    /** Apple Identity Token 기반 로그인 */
    public JwtToken login(String identityToken) {
        log.info("Apple OAuth 로그인 시작");

        AppleUserInfo appleUserInfo = verifyAndDecodeIdentityToken(identityToken);

        // Apple은 이름을 항상 주지 않으므로 기본값으로 sub를 사용 (추후 프로필에서 수정)
        String name = appleUserInfo.getSub();

        User user = userService.getOrCreateUser(
            appleUserInfo.getEmail(),
            name,
            "APPLE"
        );

        JwtToken jwtToken = tokenService.generateTokens(user.getUserId());

        log.info("Apple OAuth 로그인 완료: userId={}, email={}", user.getUserId(), user.getEmail());
        return jwtToken;
    }

    /**
     * Identity Token 디코딩 및 최소 검증
     * - JWT(payload)만 디코딩하여 sub/email 추출
     * - 형식/필수 필드 정도만 확인
     */
    private AppleUserInfo verifyAndDecodeIdentityToken(String identityToken) {
        try {
            String[] parts = identityToken.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("유효하지 않은 Identity Token 형식");
            }

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            AppleUserInfo userInfo = objectMapper.readValue(payload, AppleUserInfo.class);

            if (userInfo.getSub() == null || userInfo.getEmail() == null) {
                throw new IllegalArgumentException("Identity Token에 필수 정보 없음");
            }

            log.debug("Apple Identity Token 디코딩 성공: email={}", userInfo.getEmail());
            return userInfo;

        } catch (Exception e) {
            log.error("Apple Identity Token 처리 실패", e);
            throw new RuntimeException("Apple Identity Token 처리 중 오류 발생", e);
        }
    }

    /**
     * TODO(운영): Apple 공개키로 서명 검증
     * - https://appleid.apple.com/auth/keys
     */
    @SuppressWarnings("unused")
    private boolean verifyIdentityTokenSignature(String identityToken) {
        log.warn("Apple Identity Token 서명 검증 미구현 (운영 배포 전 구현 필요)");
        return true;
    }
}
