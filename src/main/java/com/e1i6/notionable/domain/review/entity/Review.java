package com.e1i6.notionable.domain.review.entity;

import com.e1i6.notionable.domain.review.data.ReviewDto;
import com.e1i6.notionable.domain.review.data.ReviewUpdateDto;
import com.e1i6.notionable.domain.template.entity.Template;
import com.e1i6.notionable.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private Template template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String rate;

    private String content;

    @ElementCollection
    private List<String> images = new ArrayList<>();

    public void updateReview(ReviewUpdateDto reqDto) {
        this.rate = reqDto.getRate();
        this.content = reqDto.getContent();
        this.images = reqDto.getImages();
    }

    public static ReviewDto toReviewDto(Review review, User user, List<String> imageUrls) {
        return ReviewDto.builder()
                .reviewId(review.getReviewId())
                .nickName(user.getNickName())
                .profile(user.getProfile())
                .content(review.getContent())
                .rate(review.getRate())
                .imageUrls(imageUrls)
                .build();
    }
}
