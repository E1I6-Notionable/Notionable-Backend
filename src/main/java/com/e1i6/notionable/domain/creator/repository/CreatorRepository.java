package com.e1i6.notionable.domain.creator.repository;

import com.e1i6.notionable.domain.creator.entity.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, Long> {
    Optional<Creator> findByUserUserId(Long userId);
}
