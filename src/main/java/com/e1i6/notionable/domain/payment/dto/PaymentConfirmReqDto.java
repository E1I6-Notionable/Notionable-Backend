package com.e1i6.notionable.domain.payment.dto;

import lombok.Data;

@Data
public class PaymentConfirmReqDto {
    private String orderId;
    private Integer amount;
    private String paymentKey;
    private Long sellerId;
    private Long templateId;
}
