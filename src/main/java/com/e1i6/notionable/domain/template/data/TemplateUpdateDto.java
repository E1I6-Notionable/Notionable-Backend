package com.e1i6.notionable.domain.template.data;

import lombok.Data;

import java.util.List;

@Data
public class TemplateUpdateDto {
    private String title;
    private String content;
    private String category;
    private Integer price;
    private String notionUrl;
    private String thumbnail;
    private List<String> imageUrls;

    public TemplateUpdateDto(TemplateUpdateReqDto reqDto, String thumbnail, List<String> images) {
        this.title = reqDto.getTitle();
        this.content = reqDto.getContent();
        this.category = reqDto.getCategory();
        this.price = reqDto.getPrice();
        this.notionUrl = reqDto.getNotionUrl();
        this.thumbnail = thumbnail;
        this.imageUrls = images;
    }
}
