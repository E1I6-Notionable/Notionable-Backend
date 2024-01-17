package com.e1i6.notionable.domain.community.repository;

import com.e1i6.notionable.domain.community.entity.Community;
import com.e1i6.notionable.domain.community.entity.CommunityLike;
import com.e1i6.notionable.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<CommunityLike, Long> {
    boolean existsByUserAndCommunity(User user, Community community);
    Long countByCommunity(Community community);
    void deleteByUserAndCommunity(User user, Community community);

}
