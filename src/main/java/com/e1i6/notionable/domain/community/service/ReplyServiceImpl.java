package com.e1i6.notionable.domain.community.service;

import com.e1i6.notionable.domain.community.dto.ReplyReq;
import com.e1i6.notionable.domain.community.dto.ReplyRes;
import com.e1i6.notionable.domain.community.entity.CommunityReply;
import com.e1i6.notionable.domain.community.entity.CommunityComment;
import com.e1i6.notionable.domain.community.repository.CommentRepository;
import com.e1i6.notionable.domain.community.repository.ReplyRepository;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    //대댓글 작성
    public Long addReply(Long userId, Long commentId, ReplyReq replyReq){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_USER));

        CommunityComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_COMMENT));

        CommunityReply reply = CommunityReply.builder()
                .content(replyReq.getContent())
                .user(user)
                .communityComment(comment)
                .community(comment.getCommunity())
                .build();

        CommunityReply savedReply = replyRepository.save(reply);

        return savedReply.getCommunityReplyId();
    }

    //대댓글 목록 조회
    public ReplyRes.ReplyListRes getAllReply(Long commentId, Pageable pageable) {
        CommunityComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_COMMENT));
        Page<CommunityReply> allReply = replyRepository.findByCommunityComment(comment, pageable);
        return ReplyRes.ReplyListRes.of(allReply);
    }


    //대댓글 삭제
    @Transactional
    public String deleteReply(Long userId, Long replyId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_USER));

        log.info(String.valueOf(replyId));
        CommunityReply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_REPLY));

        if (!user.equals(reply.getUser())) {
            throw new ResponseException(ResponseCode.NO_AUTHORITY);
        }

        replyRepository.delete(reply);
        return "대댓글이 삭제되었습니다.";

    }
}
