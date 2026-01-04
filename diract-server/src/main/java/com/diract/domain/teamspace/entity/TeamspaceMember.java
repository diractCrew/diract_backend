package com.diract.domain.teamspace.entity;

import com.google.cloud.Timestamp;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamspaceMember {

    private String userId;
    private Timestamp joinedAt;
}