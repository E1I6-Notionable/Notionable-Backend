package com.e1i6.notionable.domain.community.dto;

import com.e1i6.notionable.domain.community.entity.Community;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.global.common.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Data
@Getter
public class CommunityRes{
    private Long communityId;
    private String category;
    private Long communityLike;
    private String title;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;


    public CommunityRes(Long communityId, String category, Long communityLike, String title, String content, Long userId, LocalDateTime createdAt) {
        this.communityId = communityId;
        this.category = category;
        this.communityLike = communityLike;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
    }

}
