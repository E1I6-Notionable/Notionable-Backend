package com.e1i6.notionable.domain.inquiryanswer.inquiry.dto;

import com.e1i6.notionable.domain.inquiryanswer.answer.entity.Answer;
import com.e1i6.notionable.domain.inquiryanswer.inquiry.entity.Inquiry;
import com.e1i6.notionable.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long user_id;
    private Long creator_id;
    private Long template_id;

    public static InquiryDto toInquiryDto(Inquiry inquiry) {
        InquiryDto inquiryDto = new InquiryDto();

        inquiryDto.setInquiry_id(inquiry.getInquiry_id());
        inquiryDto.setTitle(inquiry.getTitle());
        inquiryDto.setContent(inquiry.getContent());
        inquiryDto.setFileUrl(inquiry.getFileUrl());
        inquiryDto.setCreatedAt(inquiry.getCreatedAt());
        inquiryDto.setStatus(inquiry.getStatus());
        inquiryDto.setUser_id(inquiry.getUser().getUserId());
        inquiryDto.setCreator_id(inquiry.getCreator_id());
        inquiryDto.setTemplate_id(inquiry.getTemplate_id());

        return inquiryDto;
    }
}
