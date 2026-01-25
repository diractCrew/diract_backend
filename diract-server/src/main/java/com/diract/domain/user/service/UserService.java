package com.diract.domain.user.service;

import com.diract.domain.user.entity.User;
import com.diract.domain.user.repository.UserRepository;
import com.google.cloud.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 사용자 도메인 서비스
 * - OAuth 로그인 사용자 조회/생성
 * - 프로필 수정 / FCM 토큰 업데이트
 * - 회원 탈퇴(Soft Delete)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * OAuth 로그인 사용자 조회 또는 생성
     * - 이메일로 사용자 조회(삭제되지 않은 사용자만)
     * - 있으면 lastLoginAt 갱신 후 반환
     * - 없으면 신규 사용자 생성 후 반환
     */
    public User getOrCreateUser(String email, String name, String loginType) {
        return userRepository.findByEmail(email)
            .filter(user -> !user.isDeleted())
            .map(existingUser -> {
                existingUser.setLastLoginAt(Timestamp.now());
                userRepository.save(existingUser);
                log.info("기존 사용자 로그인: userId={}, email={}", existingUser.getUserId(), email);
                return existingUser;
            })
            .orElseGet(() -> {
                User newUser = createUser(email, name, loginType);
                log.info("신규 사용자 생성: userId={}, email={}", newUser.getUserId(), email);
                return newUser;
            });
    }

    /** 신규 사용자 생성 */
    private User createUser(String email, String name, String loginType) {
        Timestamp now = Timestamp.now();

        User user = User.builder()
            .userId(generateUserId())
            .email(email)
            .name(name)
            .loginType(loginType)
            .status("ACTIVE")
            .termsAgreed(true)
            .privacyAgreed(true)
            .lastLoginAt(now)
            .build();

        // BaseEntity 필드 수동 설정
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        return userRepository.save(user);
    }

    /**
     * userId로 사용자 조회
     * - 삭제된 사용자는 조회하지 않음
     */
    public User findById(String userId) {
        return userRepository.findById(userId)
            .filter(user -> !user.isDeleted())
            .orElseThrow(() -> {
                log.warn("사용자를 찾을 수 없음: userId={}", userId);
                return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
            });
    }

    /** 사용자 정보 수정 */
    public User updateUser(User user) {
        // 삭제되지 않은 사용자만 수정 가능
        findById(user.getUserId());

        user.setUpdatedAt(Timestamp.now());
        return userRepository.save(user);
    }

    /** FCM 토큰 업데이트 */
    public void updateFcmToken(String userId, String fcmToken) {
        User user = findById(userId);

        user.setFcmToken(fcmToken);
        user.setUpdatedAt(Timestamp.now());
        userRepository.save(user);

        log.debug("FCM 토큰 업데이트: userId={}", userId);
    }

    /**
     * 회원 탈퇴 (Soft Delete)
     * - 실제 삭제하지 않고 deletedAt 기록
     */
    public void deleteUser(String userId) {
        User user = findById(userId);

        user.softDelete();
        userRepository.save(user);

        log.info("회원 탈퇴 완료 (Soft Delete): userId={}", userId);
    }

    /** UUID 기반 userId 생성 */
    private String generateUserId() {
        String userId;
        do {
            userId = "user_" + UUID.randomUUID();
        } while (userRepository.existsById(userId));

        return userId;
    }

    public List<User> getAllUsers() {
        return userRepository.findAllNotDeleted();
    }
}
