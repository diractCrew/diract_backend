package com.diract.domain.user.entity;

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
public class UserTeamspace {

    private String teamspaceId;
    private Timestamp joinedAt;
}