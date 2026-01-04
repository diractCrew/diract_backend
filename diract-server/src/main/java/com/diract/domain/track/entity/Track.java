package com.diract.domain.track.entity;

import com.diract.global.common.BaseEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Track extends BaseEntity {

    private String tracksId;
    private String trackName;
    private String projectId;
    private String creatorId;
}