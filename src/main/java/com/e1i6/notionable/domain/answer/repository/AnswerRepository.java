package com.e1i6.notionable.domain.answer.repository;

import com.e1i6.notionable.domain.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
