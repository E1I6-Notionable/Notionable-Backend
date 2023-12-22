package com.e1i6.notionable.domain.template.repository;

import com.e1i6.notionable.domain.template.entity.FreeTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreeTemplateRepository extends JpaRepository<FreeTemplate, Long> {

    List<FreeTemplate> findTop5ByOrderByCreatedAtDesc();
}
