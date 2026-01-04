package com.diract.domain.invite.entity;

import com.google.cloud.Timestamp;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invite {

    private String inviteId;
    private String token;
    private String teamspaceId;
    private String inviterId;
    private String role;
    private String status;
    private Integer uses;
    private Timestamp expiresAt;
    private Timestamp createdAt;
}