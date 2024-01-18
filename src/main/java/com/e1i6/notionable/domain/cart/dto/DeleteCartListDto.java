package com.e1i6.notionable.domain.cart.dto;

import com.e1i6.notionable.domain.cart.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCartListDto {
    private List<Long> item_id;
}
