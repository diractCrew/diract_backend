package com.diract.domain.user.entity;

import com.diract.global.common.BaseEntity;
import com.google.cloud.Timestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    private String userId;
    private String email;
    private String name;
    private String loginType;
    private String status;
    private Boolean termsAgreed;
    private Boolean privacyAgreed;
    private Timestamp lastLoginAt;
    private String fcmToken;
}