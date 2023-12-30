package com.e1i6.notionable.domain.template.data.dto;

import lombok.Data;

import java.util.List;

@Data
public class UploadFreeTemplateReqDto {
    private String title;
    private String content;
    private String category;
    private String notionUrl;
}
