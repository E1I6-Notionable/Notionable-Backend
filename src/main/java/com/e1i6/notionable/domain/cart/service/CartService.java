package com.e1i6.notionable.domain.cart.service;

import com.e1i6.notionable.domain.cart.Entity.Cart;
import com.e1i6.notionable.domain.cart.Repository.CartRepository;
import com.e1i6.notionable.domain.cart.dto.CartDto;
import com.e1i6.notionable.domain.template.data.dto.FreeTemplateDto;
import com.e1i6.notionable.domain.template.entity.FreeTemplate;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    public List<CartDto> getMyCartInformation(Long userId) {
        List<Cart> cartItems = cartRepository.findListByUserUserId(userId);
        List<CartDto> cartItemsDtos = new ArrayList<>();

        for(Cart cartItem : cartItems){
            CartDto cartDto = CartDto.builder()
                    .itemId(cartItem.getItemId())
                    .thumbnail(cartItem.getThumbnail())
                    .creator(cartItem.getCreator())
                    .attribute(cartItem.getAttribute())
                    .template_url(cartItem.getTemplate_url())
                    .price(cartItem.getPrice())
                    .title(cartItem.getTitle())
                    .build();

            cartItemsDtos.add(cartDto);
        }

        return cartItemsDtos;
    }

    public CartDto addMyCartInformation(Long userId, CartDto cartDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Cart cart = Cart.builder()
                .title(cartDto.getTitle())
                .price(cartDto.getPrice())
                .thumbnail(cartDto.getThumbnail())
                .attribute(cartDto.getAttribute())
                .creator(cartDto.getCreator())
                .template_url(cartDto.getTemplate_url())
                .user(user)
                .build();

        Cart savedCart = cartRepository.save(cart);

        return CartDto.mapCartToDto(savedCart);
    }
}
