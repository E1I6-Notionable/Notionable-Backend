package com.e1i6.notionable.domain.inquiryanswer.inquiry.repository;

import com.e1i6.notionable.domain.inquiryanswer.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByUser_UserId(Long userId);

    @Query("SELECT i FROM Inquiry i WHERE i.creator_id = :creatorId")
    List<Inquiry> findInquiriesByCreatorId(@Param("creatorId") Long creatorId);
}