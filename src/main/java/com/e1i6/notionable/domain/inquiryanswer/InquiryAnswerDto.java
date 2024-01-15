package com.e1i6.notionable.domain.inquiryanswer;

import com.e1i6.notionable.domain.inquiryanswer.answer.dto.AnswerDto;
import com.e1i6.notionable.domain.inquiryanswer.inquiry.dto.InquiryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryAnswerDto {
    private InquiryDto inquiry;
    private AnswerDto answer;
}
