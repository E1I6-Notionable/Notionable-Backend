package com.e1i6.notionable.domain.community.service;

import com.e1i6.notionable.domain.community.dto.community.CommentReq;

public interface CommentService {
    Long addComment(Long userId, Long communityId, CommentReq commentReq);
}
