package com.diract.domain.user.dto;

import com.diract.domain.user.entity.User;
import com.google.cloud.Timestamp;
import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 응답 DTO
 * - User 엔티티를 클라이언트 응답 형태로 변환
 */
@Getter
@Builder
public class UserResponse {

    private String userId;
    private String email;
    private String name;
    private String loginType; // TODO: enum 전환 고려
    private String status;    // TODO: enum 전환 고려
    private Timestamp lastLoginAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    /** User -> UserResponse */
    public static UserResponse from(User user) {
        return UserResponse.builder()
            .userId(user.getUserId())
            .email(user.getEmail())
            .name(user.getName())
            .loginType(user.getLoginType())
            .status(user.getStatus())
            .lastLoginAt(user.getLastLoginAt())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }

    /** 공개 프로필 응답 */
    public static UserResponse publicProfile(User user) {
        return UserResponse.builder()
            .userId(user.getUserId())
            .name(user.getName())
            .build();
    }
}
