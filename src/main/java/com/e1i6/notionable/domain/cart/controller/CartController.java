package com.e1i6.notionable.domain.cart.controller;

import com.e1i6.notionable.domain.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class CartController {

    private final CartService cartService;

    @GetMapping("/my-cart")
    public BaseResponse<>
}
