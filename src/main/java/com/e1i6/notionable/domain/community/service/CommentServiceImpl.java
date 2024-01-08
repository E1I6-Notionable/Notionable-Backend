package com.e1i6.notionable.domain.community.service;

import com.e1i6.notionable.domain.community.dto.community.CommentReq;
import com.e1i6.notionable.domain.community.entity.Community;
import com.e1i6.notionable.domain.community.entity.CommunityComment;
import com.e1i6.notionable.domain.community.repository.CommentRepository;
import com.e1i6.notionable.domain.community.repository.CommunityRepository;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Long addComment(Long userId, Long communityId, CommentReq commentReq){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found with id: " + communityId));

        CommunityComment comment = CommunityComment.builder()
                .content(commentReq.getContent())
                .user(user)
                .community(community)
                .build();

        CommunityComment savedComment = commentRepository.save(comment);
        community.addComment();

        return savedComment.getCommunityCommentId();
    }
}
