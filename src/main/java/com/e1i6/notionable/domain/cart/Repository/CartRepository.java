package com.e1i6.notionable.domain.cart.Repository;

import com.e1i6.notionable.domain.cart.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
