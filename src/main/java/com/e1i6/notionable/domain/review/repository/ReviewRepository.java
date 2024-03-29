package com.e1i6.notionable.domain.review.repository;

import com.e1i6.notionable.domain.review.entity.Review;
import com.e1i6.notionable.domain.template.entity.Template;
import com.e1i6.notionable.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByTemplate(Template template);
    List<Review> findAllByUser(User user);
}
