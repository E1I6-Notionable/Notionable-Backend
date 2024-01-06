package com.e1i6.notionable.domain.community.dto;

import com.e1i6.notionable.domain.cart.Entity.Cart;
import com.e1i6.notionable.domain.cart.dto.CartDto;
import com.e1i6.notionable.domain.community.entity.Community;
import com.e1i6.notionable.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
