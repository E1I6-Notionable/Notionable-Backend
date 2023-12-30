package com.e1i6.notionable.domain.template.data.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FreeTemplateDto {
    private Long freeTemplateId;
    private String nickName;
    private String profile;
    private String title;
    private String content;
    private String category;
    private String thumbnail;
    private String createdAt;
}
