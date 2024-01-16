package com.e1i6.notionable.domain.community.repository;

import com.e1i6.notionable.domain.community.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    @Query("select m from Community m where (:keyword is null or m.content like %:keyword% or m.title like %:keyword%) and " +
            "(:filter is null or m.category = :filter)")
    Page<Community> findByKeywordAndFilter(@Param("keyword") String keyword,
                                                 @Param("filter") String filter,
                                                 Pageable pageable);

    Page<Community> findByUser_UserId(Long userId, Pageable pageable);

    @Query("SELECT c FROM Community c WHERE c.createdAt >= CURRENT_DATE - 5 ORDER BY c.communityLike DESC, c.createdAt DESC")
    List<Community> findTop5ByCreatedDateAndOrderByCommunityLikeDesc();

}
