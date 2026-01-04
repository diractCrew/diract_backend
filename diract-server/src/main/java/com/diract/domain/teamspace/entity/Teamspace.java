package com.diract.domain.teamspace.entity;

import com.diract.global.common.BaseEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teamspace extends BaseEntity {

    private String teamspaceId;
    private String teamspaceName;
    private String ownerId;
}