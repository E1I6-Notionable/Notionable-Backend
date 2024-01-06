package com.e1i6.notionable.domain.community.dto.community;

import lombok.Builder;
import lombok.Data;

//게시물 작성 형식
@Data
@Builder
public class CommunityReq {
    private String category;
    private String title;
    private String content;

}
