package com.e1i6.notionable.domain.community.dto;

import com.e1i6.notionable.domain.community.entity.Community;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommunityRes {
    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    public static class CommunityInfo {
        private Long communityId;
        private String category;
        private Long communityLike;
        private Long communityComment;
        private String title;
        private String content;
        private String thumbnail;
        private LocalDateTime createdAt;

        public static List<CommunityInfo> of(List<Community> community){
            return community.stream()
                    .map(CommunityInfo::of)
                    .collect(Collectors.toList());
        }

        public static CommunityInfo of(Community community){
            return CommunityInfo.builder()
                    .communityId(community.getCommunityId())
                    .category(community.getCategory())
                    .communityLike(community.getCommunityLike())
                    .communityComment(community.getCommunityComment())
                    .title(community.getTitle())
                    .content(community.getContent())
                    .thumbnail(community.getThumbnail())
                    .createdAt(community.getCreatedAt())
                    .build();
        }

    }

    //게시글 목록 조회에 사용
    @Getter
    @Builder
    public static class CommunityListRes{

        long totalCount;
        long maxPageCount;
        List<CommunityInfo> infoList;

        public static CommunityListRes of(Page<Community> community){
            return CommunityListRes.builder()
                    .totalCount(community.getTotalElements())
                    .maxPageCount(community.getTotalPages())
                    .infoList(CommunityInfo.of(community.getContent()))
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

        public static CommunityDetailRes of(Community community, List<String> imageUrlList){
            return CommunityDetailRes.builder()
                    .communityId(community.getCommunityId())
                    .category(community.getCategory())
                    .communityLike(community.getCommunityLike())
                    .communityComment(community.getCommunityComment())
                    .title(community.getTitle())
                    .content(community.getContent())
                    .thumbnail(community.getThumbnail())
                    .createdAt(community.getCreatedAt())
                    .username(community.getUser().getNickName())
                    .imageUrls(imageUrlList)
                    .profile(community.getUser().getProfile())
                    .build();
        }
    }


}
