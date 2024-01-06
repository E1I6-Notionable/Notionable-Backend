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

//    @Query("SELECT c FROM Community c WHERE (:keyword IS NULL OR c.content LIKE %:keyword% OR c.title LIKE %:keyword%) AND c.category = :filter")
//    Page<Community> findByKeywordAndFilter(@Param("keyword") String keyword,
//                                                 @Param("filter") String filter,
//                                                 Pageable pageable);

//    @Query("SELECT c FROM Community c WHERE (:keyword IS NULL OR c.content LIKE %:keyword%) OR (:keyword IS NULL OR c.title LIKE %:keyword%)")
//    Page<Community> findKeyword(@Param("keyword") String keyword, Pageable pageable);


    @Query("select m from Community m where (:keyword is null or m.content like %:keyword% or m.title like %:keyword%) and " +
            "(:filter is null or m.category = :filter)")
    Page<Community> findByKeywordAndFilter(@Param("keyword") String keyword,
                                                 @Param("filter") String filter,
                                                 Pageable pageable);
//    List<Community> findByTitleContaining(String keyword);

}
