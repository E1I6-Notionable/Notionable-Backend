package com.e1i6.notionable.domain.cart.repository;

import com.e1i6.notionable.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findListByUserUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.user.userId = :userId AND c.item_Id = :item_Id")
    void deleteCartItem(@Param("userId")Long userId, @Param("item_Id")Long item_Id);

    @Query("SELECT COUNT(c) > 0 FROM Cart c WHERE c.user.userId = :userId AND c.item_Id = :item_Id")
    boolean existsByUserIdAndItemId(@Param("userId") Long userId, @Param("item_Id") Long item_Id);
}
