package com.e1i6.notionable.domain.cart.service;

import com.e1i6.notionable.domain.cart.entity.Cart;
import com.e1i6.notionable.domain.cart.repository.CartRepository;
import com.e1i6.notionable.domain.cart.dto.CartDto;
import com.e1i6.notionable.domain.template.repository.TemplateRepository;
import com.e1i6.notionable.domain.user.entity.User;
import com.e1i6.notionable.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;

    public List<CartDto> getMyCartInformation(Long userId) {
        List<Cart> cartItems = cartRepository.findListByUserUserId(userId);
        List<CartDto> cartItemsDtos = new ArrayList<>();

        for(Cart cartItem : cartItems){
            CartDto cartDto = CartDto.builder()
                    .item_id(cartItem.getItem_Id())
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
        cartDto.setThumbnail(templateRepository.findThumbnailByTemplateId(cartDto.getTemplate_id()));

        Cart cart = Cart.builder()
                .title(cartDto.getTitle())
                .price(cartDto.getPrice())
                .thumbnail(cartDto.getThumbnail())
                .attribute(cartDto.getAttribute())
                .creator(cartDto.getCreator())
                .template_url(cartDto.getTemplate_url())
                .user(user)
                .template_id(cartDto.getTemplate_id())
                .build();

        Cart savedCart = cartRepository.save(cart);

        return CartDto.mapCartToDto(savedCart);
    }

    public String deleteMyCartInformation(Long user_id, List<Long> item_Id) {
        log.info("item_id = {}", item_Id);

        cartRepository.deleteCartItem(user_id, item_Id);
        return "Deleted successfully";
    }
}
