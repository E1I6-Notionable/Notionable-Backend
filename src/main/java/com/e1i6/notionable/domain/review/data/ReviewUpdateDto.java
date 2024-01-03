package com.e1i6.notionable.domain.review.data;

import lombok.Data;

import java.util.List;

@Data
public class ReviewUpdateDto {
    private String rate;
    private String content;
    private List<String> imageUrls;

    public ReviewUpdateDto(ReviewUpdateReqDto reqDto, List<String> images) {
        this.rate = reqDto.getRate();
        this.content = reqDto.getContent();
        this.imageUrls = images;
    }
}
