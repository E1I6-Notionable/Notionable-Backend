package com.e1i6.notionable.domain.community.dto;

import com.e1i6.notionable.domain.community.entity.Community;
import com.e1i6.notionable.domain.community.repository.LikeRepository;
import com.e1i6.notionable.domain.user.entity.User;
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
        private boolean existLike;

        // + top5 게시글 목록 조회에 사용
        public static List<CommunityInfo> of(List<Community> communityList, LikeRepository likeRepository, User user){
            return communityList.stream()
                    .map(community -> of(community, likeRepository, user))
                    .collect(Collectors.toList());
        }

        public static CommunityInfo of(Community community, LikeRepository likeRepository, User user){
            boolean likedByUser = likeRepository.existsByUserAndCommunity(user, community);
            return CommunityInfo.builder()
                    .communityId(community.getCommunityId())
                    .category(community.getCategory())
                    .communityLike(community.getCommunityLike())
                    .communityComment(community.getCommunityComment())
                    .title(community.getTitle())
                    .content(community.getContent())
                    .thumbnail(community.getThumbnail())
                    .createdAt(community.getCreatedAt())
                    .existLike(likedByUser)
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

        public static CommunityListRes of(Page<Community> community, LikeRepository likeRepository, User user){
            return CommunityListRes.builder()
                    .totalCount(community.getTotalElements())
                    .maxPageCount(community.getTotalPages())
                    .infoList(CommunityInfo.of(community.getContent(), likeRepository, user))
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

        public static CommunityDetailRes of(Community community, List<String> imageUrlList, boolean existLike){
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
                    .existLike(existLike)
                    .build();
        }
    }


}
