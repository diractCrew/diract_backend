package com.diract.domain.user.controller;

import com.diract.domain.user.dto.UserResponse;
import com.diract.domain.user.dto.UserUpdateRequest;
import com.diract.domain.user.entity.User;
import com.diract.domain.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 API
 * - 내 정보 조회/수정/탈퇴
 * - 타인 프로필 조회(공개 정보만)
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** 내 정보 조회 */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(
        @AuthenticationPrincipal String userId
    ) {
        log.info("내 정보 조회 요청: userId={}", userId);

        User user = userService.findById(userId);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    /** 내 프로필 수정 */
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyProfile(
        @AuthenticationPrincipal String userId,
        @RequestBody UserUpdateRequest request
    ) {
        log.info("프로필 수정 요청: userId={}", userId);

        User user = userService.findById(userId);

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getFcmToken() != null) {
            user.setFcmToken(request.getFcmToken());
        }

        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(UserResponse.from(updatedUser));
    }

    /** 회원 탈퇴 (Soft Delete) */
    @DeleteMapping("/me")
    public ResponseEntity<DeleteResponse> deleteMyAccount(
        @AuthenticationPrincipal String userId
    ) {
        log.info("회원 탈퇴 요청: userId={}", userId);

        userService.deleteUser(userId);
        return ResponseEntity.ok(new DeleteResponse(true, "회원 탈퇴가 완료되었습니다."));
    }

    /** 타인 프로필 조회 */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(
        @PathVariable String userId
    ) {
        log.info("사용자 프로필 조회 요청: userId={}", userId);

        User user = userService.findById(userId);
        return ResponseEntity.ok(UserResponse.publicProfile(user));
    }

    public record DeleteResponse(boolean success, String message) {}

    /** 전체 사용자 조회 (Soft Delete 제외) */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("전체 사용자 조회 요청");

        List<UserResponse> users = userService.getAllUsers()
            .stream()
            .map(UserResponse::from)
            .toList();

        return ResponseEntity.ok(users);
    }
}
