package com.e1i6.notionable.domain.review.data;

import lombok.Data;

import java.util.List;

@Data
public class ReviewUpdateReqDto {
    private String rate;
    private String content;
    private List<String> imageUrls;
}
