package com.e1i6.notionable.domain.template.data;

import lombok.Data;
import lombok.Getter;

@Data
public class TemplateUploadReqDto {
    private String title;
    private String content;
    private String category;
    private Integer price;
    private String notionUrl;
}
