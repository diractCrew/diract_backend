package com.diract.domain.project.entity;

import com.diract.global.common.BaseEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project extends BaseEntity {

    private String projectId;
    private String projectName;
    private String creatorId;
    private String teamspaceId;
}