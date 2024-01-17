package com.e1i6.notionable.domain.community.service;

import com.e1i6.notionable.domain.community.dto.CommentReq;
import com.e1i6.notionable.domain.community.dto.CommentRes;
import com.e1i6.notionable.domain.community.entity.Community;
import com.e1i6.notionable.domain.community.entity.CommunityComment;
import com.e1i6.notionable.domain.community.repository.CommentRepository;
import com.e1i6.notionable.domain.community.repository.CommunityRepository;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import com.e1i6.notionable.global.common.response.ResponseCode;
import com.e1i6.notionable.global.common.response.ResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;

    //댓글 작성
    public Long addComment(Long userId, Long communityId, CommentReq commentReq){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_USER));

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_COMMUNITY));

        CommunityComment comment = CommunityComment.builder()
                .content(commentReq.getContent())
                .user(user)
                .community(community)
                .build();

        CommunityComment savedComment = commentRepository.save(comment);

        return savedComment.getCommunityCommentId();
    }

    //댓글 목록 조회
    public CommentRes.CommentListRes getAllComment(Long communityId, Pageable pageable) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_COMMUNITY));
        Page<CommunityComment> allComment = commentRepository.findByCommunity(community, pageable);
        return CommentRes.CommentListRes.of(allComment);
    }
}
