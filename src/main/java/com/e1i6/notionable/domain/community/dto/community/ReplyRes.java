package com.e1i6.notionable.domain.community.dto.community;

import com.e1i6.notionable.domain.community.entity.CommunityReply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ReplyRes {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class  ReplyInfo {
        Long replyId;
        String content;
        LocalDateTime createdAt;
        String username;
        String profile;

        public static List<ReplyRes.ReplyInfo> of(List<CommunityReply> commentReplies){
            return commentReplies.stream()
                    .map(ReplyRes.ReplyInfo::of)
                    .collect(Collectors.toList());
        }

        public static ReplyRes.ReplyInfo of(CommunityReply reply){
            return ReplyRes.ReplyInfo.builder()
                    .replyId(reply.getCommunityReplyId())
                    .content(reply.getContent())
                    .createdAt(reply.getCreatedAt())
                    .username(reply.getUser().getNickName())
                    .profile(reply.getUser().getProfile())
                    .build();
        }
    }

    //댓글 전체 대댓글 조회
    @Getter
    @Builder
    public static class ReplyListRes{

        long totalCount;
        long maxPageCount;
        List<ReplyRes.ReplyInfo> infoList;

        public static ReplyRes.ReplyListRes of(Page<CommunityReply> commentReplies){
            return ReplyRes.ReplyListRes.builder()
                    .totalCount(commentReplies.getTotalElements())
                    .maxPageCount(commentReplies.getTotalPages())
                    .infoList(ReplyInfo.of(commentReplies.getContent()))
                    .build();
        }
    }
}
