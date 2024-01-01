package com.e1i6.notionable.domain.template.data.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UploadTemplateReqDto {
    private String title;
    private String content;
    private String category;
    private Integer price;
    private String notionUrl;
}
