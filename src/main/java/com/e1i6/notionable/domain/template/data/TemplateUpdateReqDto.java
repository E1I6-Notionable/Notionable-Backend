package com.e1i6.notionable.domain.template.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TemplateUpdateReqDto {
    private String title;
    private String content;
    private String category;
    private Integer price;
    private String notionUrl;
    private List<String> imageUrls;
}
