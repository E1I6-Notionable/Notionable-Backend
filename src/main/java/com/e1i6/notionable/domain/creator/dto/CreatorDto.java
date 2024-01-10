package com.e1i6.notionable.domain.creator.dto;

import com.e1i6.notionable.domain.creator.entity.Creator;
import com.e1i6.notionable.domain.user.data.dto.UserDto;
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
    private String bankPaper; // 통장 사본 링크
    private String identification; // 신분증 사본 링크
    private String status; // accepted, rejected, pending

    public static CreatorDto toCreatorDto(Creator creator) {
        CreatorDto creatorDto = new CreatorDto();
        creatorDto.setUser(creator.getUser());
        creatorDto.setCreatorType(creator.getCreatorType());
        creatorDto.setBank(creator.getBank());
        creatorDto.setAccountNumber(creator.getAccountNumber());
        creatorDto.setBankPaper(creator.getBankPaper());
        creatorDto.setIdentification(creator.getIdentification());
        creatorDto.setStatus(creator.getStatus());

        return creatorDto;
    }
}
