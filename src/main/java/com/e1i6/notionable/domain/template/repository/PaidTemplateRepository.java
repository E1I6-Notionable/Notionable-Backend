package com.e1i6.notionable.domain.template.repository;

import com.e1i6.notionable.domain.template.entity.FreeTemplate;
import com.e1i6.notionable.domain.template.entity.PaidTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaidTemplateRepository extends JpaRepository<PaidTemplate, Long> {
    List<PaidTemplate> findTop5ByOrderByCreatedAtDesc();
    Page<PaidTemplate> findAll(Pageable pageable);
    Page<PaidTemplate> findAllByCategory(String category, Pageable pageable);
}
