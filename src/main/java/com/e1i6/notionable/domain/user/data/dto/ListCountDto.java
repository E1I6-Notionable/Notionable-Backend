package com.e1i6.notionable.domain.user.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListCountDto {
    private Integer paymentCount;
    private Integer postCount;
    private Integer inquiryCount;
}
