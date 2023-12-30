package com.e1i6.notionable.domain.cart.Repository;

import com.e1i6.notionable.domain.cart.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findListByUserUserId(Long userId);

    //Cart findByUserUserId(Long userId);
}
