package com.e1i6.notionable.domain.community.repository;

import com.e1i6.notionable.domain.community.entity.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<CommentReply, Long> {
}
