package com.e1i6.notionable.domain.template.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaidTemplateDto {
    private Long paidTemplateId;
    private String nickName;
    private String profile;
    private String title;
    private String content;
    private String category;
    private Integer price;
    private String thumbnail;
    private String createdAt;
}
