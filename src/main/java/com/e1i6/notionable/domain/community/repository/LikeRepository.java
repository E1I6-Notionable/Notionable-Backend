package com.e1i6.notionable.domain.community.repository;

import com.e1i6.notionable.domain.community.entity.CommunityLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<CommunityLike, Long> {
}
