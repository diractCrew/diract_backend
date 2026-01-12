package com.diract.domain.inquiry.entity;

import com.google.cloud.Timestamp;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inquiry {

    private String content;
    private String status;
    private String userId;
    private String userName;
    private Timestamp createdAt;
}