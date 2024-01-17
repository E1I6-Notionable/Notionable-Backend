package com.e1i6.notionable.domain.community.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommunityReq {
    private String category;
    private String title;
    private String content;

}
