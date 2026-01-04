package com.diract.domain.notification.entity;

import com.google.cloud.Timestamp;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    private String notificationId;
    private String content;
    private String senderId;
    private List<String> receiverIds;
    private String videoId;
    private String feedbackId;
    private String teamspaceId;
    private Timestamp createdAt;
}