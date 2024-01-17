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
    @Query("select m from Community m where (:keyword is null or m.content like %:keyword% or m.title like %:keyword%) and " +
            "(:filter is null or m.category = :filter)")
    Page<Community> findByKeywordAndFilter(@Param("keyword") String keyword,
                                                 @Param("filter") String filter,
                                                 Pageable pageable);

    Page<Community> findByUser(User user, Pageable pageable);

    @Query("SELECT c, COUNT(cl) as likeCount " +
            "FROM Community c " +
            "LEFT JOIN CommunityLike cl ON c.communityId = cl.community.communityId " +
            "WHERE c.createdAt >= CURRENT_DATE - 5 " +
            "GROUP BY c " +
            "ORDER BY likeCount DESC, c.createdAt DESC")
    List<Community> findTop5CommunitiesWithLikes();

}
