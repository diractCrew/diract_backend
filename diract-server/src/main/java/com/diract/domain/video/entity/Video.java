package com.diract.domain.video.entity;

import com.diract.global.common.BaseEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video extends BaseEntity {

    private String videoId;
    private String videoTitle;
    private String videoUrl;
    private String thumbnailUrl;
    private String uploaderId;
    private Integer videoDuration;
}