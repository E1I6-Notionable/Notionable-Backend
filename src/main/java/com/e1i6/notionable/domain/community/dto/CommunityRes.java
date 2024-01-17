package com.e1i6.notionable.domain.community.dto;

import com.e1i6.notionable.domain.community.entity.Community;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class CommunityRes {

    @Getter
    @SuperBuilder
    public static class CommunityGetInfo {
        Long communityLike;
        Long communityComment;
        boolean existLike;
    }

    @Getter
    @SuperBuilder
    public static class CommunityInfo extends CommunityGetInfo{
        private Long communityId;
        private String category;
        private String title;
        private String content;
        private String thumbnail;
        private LocalDateTime createdAt;

        public static CommunityInfo of(Community community, CommunityGetInfo communityGetInfo){
            return CommunityInfo.builder()
                    .communityId(community.getCommunityId())
                    .category(community.getCategory())
                    .title(community.getTitle())
                    .content(community.getContent())
                    .thumbnail(community.getThumbnail())
                    .createdAt(community.getCreatedAt())
                    .communityLike(communityGetInfo.getCommunityLike())
                    .communityComment(communityGetInfo.getCommunityComment())
                    .existLike(communityGetInfo.isExistLike())
                    .build();
        }

    }

    //게시글 목록 조회, 내가 쓴 게시글 조회에 사용
    @Getter
    @Builder
    public static class CommunityListRes{

        long totalCount;
        long maxPageCount;
        List<CommunityInfo> infoList;

        public static CommunityListRes of(Page<Community> community, List<CommunityInfo> communityList){
            return CommunityListRes.builder()
                    .totalCount(community.getTotalElements())
                    .maxPageCount(community.getTotalPages())
                    .infoList(communityList)
                    .build();
        }
    }

    //게시글 상세 조회에 사용
    @Getter
    @SuperBuilder
    public static class CommunityDetailRes extends CommunityInfo{

        String username;
        String profile;
        List<String> imageUrls;

        public static CommunityDetailRes of(Community community, List<String> imageUrlList, CommunityGetInfo communityGetInfo){
            return CommunityDetailRes.builder()
                    .communityId(community.getCommunityId())
                    .category(community.getCategory())
                    .communityLike(communityGetInfo.getCommunityLike())
                    .communityComment(communityGetInfo.getCommunityComment())
                    .title(community.getTitle())
                    .content(community.getContent())
                    .thumbnail(community.getThumbnail())
                    .createdAt(community.getCreatedAt())
                    .username(community.getUser().getNickName())
                    .imageUrls(imageUrlList)
                    .profile(community.getUser().getProfile())
                    .existLike(communityGetInfo.existLike)
                    .build();
        }
    }

}
