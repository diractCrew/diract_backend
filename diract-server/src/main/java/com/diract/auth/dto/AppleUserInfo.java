package com.diract.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Apple 사용자 정보 DTO
 *
 * Apple Identity Token (JWT)의 payload에 포함된 사용자 정보
 * Apple은 ID Token에 사용자 정보를 포함시킴
 */
@Getter
@NoArgsConstructor
public class AppleUserInfo {

    /**
     * Apple 사용자 고유 ID (subject)
     */
    @JsonProperty("sub")
    private String sub;

    /**
     * 이메일
     */
    @JsonProperty("email")
    private String email;

    /**
     * 이메일 인증 여부
     */
    @JsonProperty("email_verified")
    private Boolean emailVerified;

    /**
     * 실명 인증 여부 (Private Email Relay 사용 여부)
     */
    @JsonProperty("is_private_email")
    private Boolean isPrivateEmail;
}