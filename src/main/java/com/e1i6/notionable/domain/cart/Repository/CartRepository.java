package com.e1i6.notionable.domain.cart.Repository;

import com.e1i6.notionable.domain.cart.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findListByUserUserId(Long userId);

    @Query("DELETE FROM Cart WHERE user = :userId AND itemId = :itemId")
    String deleteCartItem(@Param("user_id")Long userId, @Param("item_id")Long itemId);
}
