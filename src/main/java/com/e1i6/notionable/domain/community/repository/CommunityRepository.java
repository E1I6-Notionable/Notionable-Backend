package com.e1i6.notionable.domain.community.repository;

import com.e1i6.notionable.domain.community.entity.Community;
import com.e1i6.notionable.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    @Query("SELECT m FROM Community m WHERE (:keyword IS NULL OR m.content LIKE %:keyword% OR m.title LIKE %:keyword%) AND m.category = :filter")
    Page<Community> findByKeywordAndFilter(@Param("keyword") String keyword,
                                                 @Param("filter") String filter,
                                                 Pageable pageable);

    @Query("SELECT m FROM Community m WHERE (:keyword IS NULL OR m.content LIKE %:keyword% OR m.title LIKE %:keyword%)")
    Page<Community> findByKeyword(@Param("keyword") String keyword,
                                           Pageable pageable);

}
