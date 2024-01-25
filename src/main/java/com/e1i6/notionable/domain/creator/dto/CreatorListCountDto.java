package com.e1i6.notionable.domain.creator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatorListCountDto {
    private Long sellCount;
    private Long answerCount;
    private Long inquiryCount;
}
