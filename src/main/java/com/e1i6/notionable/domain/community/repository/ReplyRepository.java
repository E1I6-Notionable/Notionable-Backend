package com.e1i6.notionable.domain.community.repository;

import com.e1i6.notionable.domain.community.entity.Community;
import com.e1i6.notionable.domain.community.entity.CommunityComment;
import com.e1i6.notionable.domain.community.entity.CommunityReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<CommunityReply, Long> {
    Page<CommunityReply> findByCommunityComment(CommunityComment communityComment, Pageable pageable);
    Long countByCommunity(Community community);

}
