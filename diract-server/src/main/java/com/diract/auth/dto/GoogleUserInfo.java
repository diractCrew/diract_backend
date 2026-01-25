package com.diract.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Google 사용자 정보 DTO
 *
 * Google API에서 반환하는 사용자 정보
 * https://www.googleapis.com/oauth2/v2/userinfo
 */
@Getter
@NoArgsConstructor
public class GoogleUserInfo {

    /**
     * Google 사용자 고유 ID
     */
    @JsonProperty("sub")
    private String id;

    /**
     * 이메일
     */
    @JsonProperty("email")
    private String email;

    /**
     * 이름
     */
    @JsonProperty("name")
    private String name;

    /**
     * 프로필 사진 URL
     */
    @JsonProperty("picture")
    private String picture;

    /**
     * 이메일 인증 여부
     */
    @JsonProperty("verified_email")
    private Boolean verifiedEmail;
}