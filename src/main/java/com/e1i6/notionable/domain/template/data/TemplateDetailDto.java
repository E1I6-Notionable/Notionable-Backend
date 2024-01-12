package com.e1i6.notionable.domain.template.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TemplateDetailDto {
    private Long templateId;
    private String nickName;
    private String profile;
    private String thumbnail;
    private String title;
    private String category;
    private Integer price;
    private String content;
    private List<String> imageUrls;
    private boolean isPaid;
    private String createdAt;
}
