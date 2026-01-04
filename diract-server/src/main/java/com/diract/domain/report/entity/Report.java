package com.diract.domain.report.entity;

import com.diract.global.common.BaseEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report extends BaseEntity {

    private String reportId;
    private String type;
    private String reportContentType;
    private String description;
    private String reporterId;
    private String reportedId;
    private String status;
    private String videoId;
    private String feedbackId;
    private String replyId;
}