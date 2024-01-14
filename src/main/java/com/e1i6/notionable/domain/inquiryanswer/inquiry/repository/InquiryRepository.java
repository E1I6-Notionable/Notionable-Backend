package com.e1i6.notionable.domain.inquiryanswer.inquiry.repository;

import com.e1i6.notionable.domain.inquiryanswer.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByUser_UserId(Long userId);
}