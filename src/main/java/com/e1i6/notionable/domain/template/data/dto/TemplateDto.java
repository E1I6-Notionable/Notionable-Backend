package com.e1i6.notionable.domain.template.data.dto;

import com.e1i6.notionable.domain.template.entity.Template;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateDto {
    private Long templateId;
    private String nickName;
    private String profile;
    private String thumbnail;
    private String title;
    private String category;
    private Integer price;
    private String createdAt;
}
