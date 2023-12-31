package com.e1i6.notionable.domain.community.service;

import com.e1i6.notionable.domain.community.dto.community.CommentReq;
import com.e1i6.notionable.domain.community.dto.community.CommentRes;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Long addComment(Long userId, Long communityId, CommentReq commentReq);
    CommentRes.CommentListRes getAllComment(Long communityId, Pageable pageable);
}
