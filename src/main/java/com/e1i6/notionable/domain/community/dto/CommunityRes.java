package com.e1i6.notionable.domain.community.dto;

import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.global.common.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
@Getter
public class CommunityRes{
    private Long community_id;
    private String category;
    private Long community_like;
    private String title;
    private String content;
    private User user;

    public CommunityRes(Long communityId, String category, Long like, String title, String content, User user) {
        this.community_id = communityId;
        this.category = category;
        this.community_like = like;
        this.title = title;
        this.content = content;
        this.user = user;
    }
}
