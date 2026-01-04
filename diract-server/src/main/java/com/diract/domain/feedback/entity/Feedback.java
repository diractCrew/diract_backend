package com.diract.domain.feedback.entity;

import com.diract.global.common.BaseEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback extends BaseEntity {

    private String feedbackId;
    private String content;
    private String authorId;
    private String videoId;
    private String teamspaceId;
    private Integer startTime;
    private List<String> taggedUserIds;
}