package com.e1i6.notionable.domain.inquiryanswer.answer.repository;

import com.e1i6.notionable.domain.inquiryanswer.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
