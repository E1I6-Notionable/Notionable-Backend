package com.e1i6.notionable.domain.inquiry.dto;

import com.e1i6.notionable.domain.answer.entity.Answer;
import com.e1i6.notionable.domain.cart.dto.CartDto;
import com.e1i6.notionable.domain.cart.entity.Cart;
import com.e1i6.notionable.domain.inquiry.entity.Inquiry;
import com.e1i6.notionable.domain.template.entity.Template;
import com.e1i6.notionable.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryDto {

    private Long inquiry_id;
    private String title;
    private String content;
    private String fileUrl;
    private LocalDateTime createdAt; // 문의 작성일
    private String status; // 답변유무 (Yes, No)
    private User user;
    private Long template_id;
    private Answer answer;

    public static InquiryDto toInquiryDto(Inquiry inquiry) {
        InquiryDto inquiryDto = new InquiryDto();

        inquiryDto.setInquiry_id(inquiry.getInquiry_id());
        inquiryDto.setTitle(inquiry.getTitle());
        inquiryDto.setContent(inquiry.getContent());
        inquiryDto.setFileUrl(inquiry.getFileUrl());
        inquiryDto.setCreatedAt(inquiry.getCreatedAt());
        inquiryDto.setStatus(inquiry.getStatus());
        inquiryDto.setUser(inquiry.getUser());
        inquiryDto.setTemplate_id(inquiry.getTemplate_id());
        inquiryDto.setAnswer(inquiry.getAnswer());

        return inquiryDto;
    }
}
