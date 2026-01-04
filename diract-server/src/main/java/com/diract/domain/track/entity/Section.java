package com.diract.domain.track.entity;

import com.diract.global.common.BaseEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section extends BaseEntity {

    private String sectionId;
    private String sectionTitle;
}