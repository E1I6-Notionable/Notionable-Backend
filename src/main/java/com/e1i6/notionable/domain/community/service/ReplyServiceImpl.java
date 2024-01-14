package com.e1i6.notionable.domain.community.service;

import com.e1i6.notionable.domain.community.dto.ReplyReq;
import com.e1i6.notionable.domain.community.dto.ReplyRes;
import com.e1i6.notionable.domain.community.entity.CommunityReply;
import com.e1i6.notionable.domain.community.entity.CommunityComment;
import com.e1i6.notionable.domain.community.repository.CommentRepository;
import com.e1i6.notionable.domain.community.repository.ReplyRepository;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
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
    @Transactional
    public Long addReply(Long userId, Long commentId, ReplyReq replyReq){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        CommunityComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));

        CommunityReply reply = CommunityReply.builder()
                .content(replyReq.getContent())
                .user(user)
                .communityComment(comment)
                .build();

        CommunityReply savedReply = replyRepository.save(reply);

        comment.getCommunity().addComment();

        return savedReply.getCommunityReplyId();
    }

    //대댓글 목록 조회
    public ReplyRes.ReplyListRes getAllReply(Long commentId, Pageable pageable) {
        CommunityComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));
        Page<CommunityReply> allReply = replyRepository.findByCommunityComment_CommunityCommentId(comment.getCommunityCommentId(), pageable);
        return ReplyRes.ReplyListRes.of(allReply);
    }
}
