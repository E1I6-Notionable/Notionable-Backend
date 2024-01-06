package com.e1i6.notionable.domain.creator.dto;

import com.e1i6.notionable.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatorDto {
    private User user;
    private String creatorType; // 입점형태(크리에이터 타입)
    private String bank; // 은행
    private String accountNumber; // 계좌번호
    private String bankPaperUrl; // 통장 사본 링크
    private String identificationUrl; // 신분증 사본 링크
    private String status; // accepted, rejected, pending
}
