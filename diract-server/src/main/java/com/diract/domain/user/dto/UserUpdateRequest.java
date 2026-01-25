package com.diract.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 프로필 수정 요청 DTO
 * - 수정 가능한 필드만 포함
 */
@Getter
@NoArgsConstructor
public class UserUpdateRequest {

    /** 이름 */
    private String name;

    /** FCM 토큰 */
    private String fcmToken;
}
