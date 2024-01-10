package com.e1i6.notionable.domain.community.dto;

import com.e1i6.notionable.domain.community.entity.Community;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommunityReq {
    private String category;
    private String title;
    private String content;

    public static CommunityReq mapCommunityReq(Community community) {
        return CommunityReq.builder()
                .category(community.getCategory())
                .title(community.getTitle())
                .content(community.getContent())
                .build();
    }

}
