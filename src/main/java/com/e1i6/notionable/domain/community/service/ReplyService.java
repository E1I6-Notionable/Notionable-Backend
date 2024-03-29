package com.e1i6.notionable.domain.community.service;

import com.e1i6.notionable.domain.community.dto.CommentReq;
import com.e1i6.notionable.domain.community.dto.ReplyReq;
import com.e1i6.notionable.domain.community.dto.ReplyRes;
import org.springframework.data.domain.Pageable;

public interface ReplyService {
    Long addReply(Long userId, Long commentId, ReplyReq replyReq);
    ReplyRes.ReplyListRes getAllReply(Long commentId, Pageable pageable);
    String deleteReply(Long userId, Long replyId);
    String modifyReply(Long userId, Long replyId, ReplyReq replyReq);
}
