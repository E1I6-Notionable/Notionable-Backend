package com.e1i6.notionable.domain.community.dto.community;

import com.e1i6.notionable.domain.community.entity.Community;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//게시글 상세 조회에 사용
@Data
@Getter
public class CommunityDetailRes {
    private Long communityId;
    private String category;
    private Long communityLike;
    private Long communityComment;
    private String title;
    private String content;
    private String username;
    private String thumbnail;
    private LocalDateTime createdAt;
    private List<String> imageUrls = new ArrayList<>();

    public CommunityDetailRes(Community community, List<String> imageUrlList) {
        this.communityId = community.getCommunityId();
        this.category = community.getCategory();
        this.communityLike = community.getCommunityLike();
        this.communityComment = community.getCommunityComment();
        this.title = community.getTitle();
        this.content = community.getContent();
        this.username = community.getUser().getNickName();
        this.thumbnail = community.getThumbnail();
        this.imageUrls.addAll(imageUrlList);
        this.createdAt = community.getCreatedAt();
    }

}
