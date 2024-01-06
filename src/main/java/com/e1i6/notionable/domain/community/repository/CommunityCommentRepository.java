package com.e1i6.notionable.domain.community.repository;

import com.e1i6.notionable.domain.community.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
}
