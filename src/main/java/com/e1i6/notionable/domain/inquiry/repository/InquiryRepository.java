package com.e1i6.notionable.domain.inquiry.repository;

import com.e1i6.notionable.domain.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
