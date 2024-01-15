package com.e1i6.notionable.domain.template.entity;

import com.e1i6.notionable.domain.review.entity.Review;
import com.e1i6.notionable.domain.template.data.TemplateDetailDto;
import com.e1i6.notionable.domain.template.data.TemplateDto;
import com.e1i6.notionable.domain.template.data.TemplateUpdateDto;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.global.common.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class
Template extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();

//    @JsonIgnore
//    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Inquiry> inquiries = new ArrayList<>();

    @NotNull
    private String title;

    @Lob
    private String content;

    @NotNull
    private String thumbnail;

    @NotNull
    private String category;

    @NotNull
    private Integer price;

    @NotNull
    private String notionUrl;

    private Integer goodRateCount;

    @ElementCollection
    private List<String> images = new ArrayList<>();

    public void updateTemplate(TemplateUpdateDto reqDto) {
        this.title = reqDto.getTitle();
        this.content = reqDto.getContent();
        this.category = reqDto.getCategory();
        this.price = reqDto.getPrice();
        this.notionUrl = reqDto.getNotionUrl();
        this.thumbnail = reqDto.getThumbnail();
        this.images = reqDto.getImageUrls();
    }

    public static TemplateDto toTemplateDto(Template template) {
        return TemplateDto.builder()
                .templateId(template.getTemplateId())
                .nickName(template.user.getNickName())
                .profile(template.user.getProfile())
                .title(template.getTitle())
                .category(template.getCategory())
                .thumbnail(template.getThumbnail())
                .price(template.getPrice())
                .createdAt(template.getCreatedAt().toString())
                .build();
    }

    public static TemplateDetailDto toDetailTemplateDto(Template template, List<String> imageUrlList, boolean isPaid) {
        return TemplateDetailDto.builder()
                .templateId(template.getTemplateId())
                .nickName(template.user.getNickName())
                .profile(template.user.getProfile())
                .title(template.getTitle())
                .category(template.getCategory())
                .thumbnail(template.getThumbnail())
                .price(template.getPrice())
                .content(template.getContent())
                .imageUrls(imageUrlList)
                .isPaid(isPaid)
                .createdAt(template.getCreatedAt().toString())
                .build();
    }

    public void plusGoodRateCount() {
        this.goodRateCount++;
    }

    public void minusGoodRateCount() {
        this.goodRateCount--;
    }
}
