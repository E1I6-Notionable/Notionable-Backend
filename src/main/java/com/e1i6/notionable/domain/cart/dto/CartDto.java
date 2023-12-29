package com.e1i6.notionable.domain.cart.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartDto {
    private Long itemId;
    private String thumbnail;
    private String creator;
    private String attribute;
    private String template_url;
    private Integer price;
    private String title;
}
