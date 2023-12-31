package com.e1i6.notionable.domain.template.repository;

import com.e1i6.notionable.domain.template.entity.Template;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    @Query("SELECT template " +
            "FROM Template template " +
            "WHERE template.price = 0 " +
            "ORDER BY template.createdAt DESC")
    List<Template> findRecentFree();

    @Query("SELECT template " +
            "FROM Template template " +
            "WHERE template.price > 0 " +
            "ORDER BY template.createdAt DESC")
    List<Template> findRecentPaid();

    Page<Template> findAll(Pageable pageable);

    Page<Template> findAllByPriceEquals(Integer price, Pageable pageable);

    Page<Template> findAllByPriceGreaterThan(Integer price, Pageable pageable);

    Page<Template> findAllByCategoryAndPriceEquals(String category, Integer price, Pageable pageable);

    Page<Template> findAllByCategoryAndPriceGreaterThan(String category, Integer price, Pageable pageable);
}
