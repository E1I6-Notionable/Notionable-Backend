package com.e1i6.notionable.domain.payment.controller;import javax.servlet.http.HttpServletRequest;import com.e1i6.notionable.domain.payment.dto.PaymentConfirmReqDto;import org.springframework.security.core.Authentication;import org.springframework.security.core.context.SecurityContextHolder;import org.springframework.web.bind.annotation.GetMapping;import org.springframework.web.bind.annotation.PostMapping;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RestController;import com.e1i6.notionable.domain.payment.dto.PaymentConfirmResDto;import com.e1i6.notionable.domain.payment.service.PaymentService;import com.e1i6.notionable.global.common.response.BaseResponse;import com.e1i6.notionable.global.common.response.ResponseCode;import com.e1i6.notionable.global.common.response.ResponseException;import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;import java.util.List;@Slf4j@RestController@RequiredArgsConstructor@RequestMapping(value = "/payments")public class PaymentController {	private final PaymentService paymentService;	@PostMapping(value = "/confirm")	public BaseResponse<PaymentConfirmResDto> paymentConfirm(			HttpServletRequest httpServletRequest,			PaymentConfirmReqDto reqDto) throws Exception {		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();		Long userId = Long.parseLong(authentication.getName());		try {			PaymentConfirmResDto paymentResDto = paymentService.paymentSuccess(userId, reqDto);			return new BaseResponse<>(paymentResDto);		} catch (ResponseException e) {			return new BaseResponse<>(e.getErrorCode(), e.getMessage());		} catch (Exception e) {			return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());		}	}	@GetMapping("/buying")	public BaseResponse<List<PaymentConfirmResDto>> getPaidList() {		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();		Long userId = Long.parseLong(authentication.getName());		try {			return new BaseResponse<>(paymentService.getPaidList(userId));		} catch (ResponseException e) {			return new BaseResponse<>(e.getErrorCode(), e.getMessage());		} catch (Exception e) {			return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());		}	}	@GetMapping("/selling")	public BaseResponse<List<PaymentConfirmResDto>> getSoldList() {		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();		Long userId = Long.parseLong(authentication.getName());		try {			return new BaseResponse<>(paymentService.getSoldList(userId));		} catch (ResponseException e) {			return new BaseResponse<>(e.getErrorCode(), e.getMessage());		} catch (Exception e) {			return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());		}	}}