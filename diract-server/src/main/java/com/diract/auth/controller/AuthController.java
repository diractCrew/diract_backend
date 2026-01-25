package com.diract.auth.controller;

import com.diract.auth.dto.JwtToken;
import com.diract.auth.service.AppleOAuthService;
import com.diract.auth.service.GoogleOAuthService;
import com.diract.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 API 컨트롤러
 *
 * - OAuth 로그인 (Google / Apple)
 * - Access Token 재발급
 * - 로그아웃 처리
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final GoogleOAuthService googleOAuthService;
    private final AppleOAuthService appleOAuthService;
    private final TokenService tokenService;

    /**
     * Google OAuth 로그인
     *
     * @param idToken Google ID Token
     * @return Access / Refresh 토큰
     */
    @PostMapping("/login/google")
    public ResponseEntity<JwtToken> googleLogin(
        @RequestParam String idToken
    ) {
        log.info("Google 로그인 요청");

        JwtToken token = googleOAuthService.login(idToken);
        return ResponseEntity.ok(token);
    }

    /**
     * Apple OAuth 로그인
     *
     * @param identityToken Apple Identity Token
     * @return Access / Refresh 토큰
     */
    @PostMapping("/login/apple")
    public ResponseEntity<JwtToken> appleLogin(
        @RequestParam String identityToken
    ) {
        log.info("Apple 로그인 요청");

        JwtToken token = appleOAuthService.login(identityToken);
        return ResponseEntity.ok(token);
    }

    /**
     * Access Token 재발급
     *
     * @param refreshToken Refresh Token
     * @return 새 Access Token
     */
    @PostMapping("/refresh")
    public ResponseEntity<JwtToken> refresh(
        @RequestParam String refreshToken
    ) {
        log.info("Access Token 재발급 요청");

        JwtToken token = tokenService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(token);
    }

    /**
     * 로그아웃
     *
     * @param refreshToken Refresh Token
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        @RequestParam String refreshToken
    ) {
        log.info("로그아웃 요청");

        tokenService.logout(refreshToken);
        return ResponseEntity.ok().build();
    }
}
