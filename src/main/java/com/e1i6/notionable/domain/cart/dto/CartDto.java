package com.e1i6.notionable.domain.cart.dto;

import com.e1i6.notionable.domain.cart.Entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long item_id;
    private Long template_id;
    private String thumbnail;
    private String creator;
    private String attribute;
    private String template_url;
    private Integer price;
    private String title;

    public static CartDto mapCartToDto(Cart cart) {
        return CartDto.builder()
                .item_id(cart.getItem_Id())
                .template_id(cart.getTemplate_id())
                .thumbnail(cart.getThumbnail())
                .creator(cart.getCreator())
                .attribute(cart.getAttribute())
                .template_url(cart.getTemplate_url())
                .price(cart.getPrice())
                .title(cart.getTitle())
                .build();
    }
}
