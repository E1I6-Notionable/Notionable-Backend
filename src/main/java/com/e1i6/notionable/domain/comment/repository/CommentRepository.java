package com.e1i6.notionable.domain.comment.repository;

import com.e1i6.notionable.domain.comment.entity.TemplateComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<TemplateComment, Long> {

}
