package com.e1i6.notionable.domain.community.dto.community;

import com.e1i6.notionable.domain.community.entity.CommunityComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentRes {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CommentInfo {
        Long commentId;
        String content;
        LocalDateTime createdAt;
        String username;
        String profile;

        public static List<CommentRes.CommentInfo> of(List<CommunityComment> communityComments){
            return communityComments.stream()
                    .map(CommentRes.CommentInfo::of)
                    .collect(Collectors.toList());
        }

        public static CommentRes.CommentInfo of(CommunityComment comment){
            return CommentInfo.builder()
                    .commentId(comment.getCommunityCommentId())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .username(comment.getUser().getNickName())
                    .profile(comment.getUser().getProfile())
                    .build();
        }
    }

    //게시글 전체 댓글 조회
    @Getter
    @Builder
    public static class CommentListRes{

        long totalCount;
        long maxPageCount;
        List<CommentRes.CommentInfo> infoList;

        public static CommentRes.CommentListRes of(Page<CommunityComment> communityComments){
            return CommentRes.CommentListRes.builder()
                    .totalCount(communityComments.getTotalElements())
                    .maxPageCount(communityComments.getTotalPages())
                    .infoList(CommentInfo.of(communityComments.getContent()))
                    .build();
        }
    }
}
