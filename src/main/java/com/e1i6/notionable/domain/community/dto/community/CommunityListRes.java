package com.e1i6.notionable.domain.community.dto.community;

import com.e1i6.notionable.domain.community.entity.Community;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
//게시글 목록 조회에 사용
@Data
@Getter
public class CommunityListRes {
    private Long communityId;
    private String category;
    private Long communityLike;
    private Long communityComment;
    private String title;
    private String content;
    private String thumbnail;
    private LocalDateTime createdAt;

    public CommunityListRes(Community community) {
        this.communityId = community.getCommunityId();
        this.category = community.getCategory();
        this.communityLike = community.getCommunityLike();
        this.communityComment = community.getCommunityComment();
        this.title = community.getTitle();
        this.content = community.getContent();
        this.thumbnail = community.getThumbnail();
        this.createdAt = community.getCreatedAt();
    }

}
