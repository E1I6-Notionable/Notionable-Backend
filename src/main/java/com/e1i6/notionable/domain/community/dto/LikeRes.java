package com.e1i6.notionable.domain.community.dto;

import lombok.Getter;

@Getter
public class LikeRes {
    Long communityId;
    Long likeCount;
    Boolean isLiked;

    public LikeRes(long communityId, long likeCount, boolean isLiked) {
        this.communityId = communityId;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }
}
