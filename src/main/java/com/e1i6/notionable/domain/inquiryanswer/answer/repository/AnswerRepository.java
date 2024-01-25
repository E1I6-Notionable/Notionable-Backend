package com.e1i6.notionable.domain.inquiryanswer.answer.repository;

import com.e1i6.notionable.domain.inquiryanswer.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("SELECT a FROM Answer a WHERE a.inquiry.inquiry_id = :inquiryId")
    Optional<Answer> findAnswerByInquiryId(@Param("inquiryId") Long inquiryId);

    @Query("SELECT COUNT(a) FROM Answer a WHERE a.creator.creator_id = :creatorId")
    Long countAnswersByCreatorId(@Param("creatorId") Long creatorId);
}
