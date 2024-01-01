package com.e1i6.notionable.domain.review.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReviewDto {
    private Long reviewId;
    private String nickName;
    private String profile;
    private String rate;
    private String content;
    private List<String> imageUrls;
}
