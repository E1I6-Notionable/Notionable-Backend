package com.e1i6.notionable.domain.review.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewUploadReqDto {
    private Long templateId;
    private String rate;
    private String content;
}
