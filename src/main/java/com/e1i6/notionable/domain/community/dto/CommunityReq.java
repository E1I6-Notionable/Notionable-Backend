package com.e1i6.notionable.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommunityReq {
    private String category;
    private String title;
    private String content;

    public CommunityReq (){}

}
