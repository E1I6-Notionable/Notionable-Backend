package com.e1i6.notionable.domain.payment.service;import java.io.InputStream;import java.io.InputStreamReader;import java.io.OutputStream;import java.io.Reader;import java.net.HttpURLConnection;import java.net.URL;import java.nio.charset.StandardCharsets;import java.util.ArrayList;import java.util.Base64;import java.util.List;import com.e1i6.notionable.domain.payment.dto.PaymentConfirmReqDto;import com.e1i6.notionable.domain.payment.entity.Payment;import com.e1i6.notionable.domain.template.entity.Template;import com.e1i6.notionable.domain.template.repository.TemplateRepository;import org.json.simple.JSONObject;import org.json.simple.parser.JSONParser;import org.springframework.beans.factory.annotation.Value;import org.springframework.stereotype.Service;import com.e1i6.notionable.domain.payment.dto.PaymentConfirmResDto;import com.e1i6.notionable.domain.payment.repository.PaymentRepository;import com.e1i6.notionable.global.common.response.ResponseCode;import com.e1i6.notionable.global.common.response.ResponseException;import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;import org.springframework.transaction.annotation.Transactional;@Slf4j@Service@RequiredArgsConstructorpublic class PaymentService {	@Value("${payments.toss.test_secret_api_key}")	private String secretKey;	private final PaymentRepository paymentRepository;	private final TemplateRepository templateRepository;	@Transactional	public PaymentConfirmResDto paymentSuccess(Long userId, PaymentConfirmReqDto reqDto) throws Exception {		String paymentKey = reqDto.getPaymentKey();		Long sellerId = reqDto.getSellerId();		Long templateId = reqDto.getTemplateId();		String orderId = reqDto.getOrderId();		Integer amount = reqDto.getAmount();		// 토스페이먼츠에서 Basic 인증 방식 사용을 위해 뒤 문자열에 : 추가		// {USERNAME}:{PASSWORD} 방식. 비밀번호는 따로 넣지 않음		// API 요청 authorization 헤더에 추가		secretKey = secretKey + ":";		Base64.Encoder encoder = Base64.getEncoder();		byte[] encodedBytes = encoder.encode(secretKey.getBytes("UTF-8"));		String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);		URL url = new URL("https://api.tosspayments.com/v1/payments/" + paymentKey);		// header, body		HttpURLConnection connection = (HttpURLConnection)url.openConnection();		connection.setRequestProperty("Authorization", authorizations);		connection.setRequestProperty("Content-Type", "application/json");		connection.setRequestMethod("POST");		connection.setDoOutput(true);		JSONObject obj = new JSONObject();		obj.put("orderId", orderId);		obj.put("amount", amount);		// POST request		OutputStream outputStream = connection.getOutputStream();		outputStream.write(obj.toString().getBytes("UTF-8"));		int code = connection.getResponseCode();		boolean isSuccess = code == 200 ? true : false;		InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();		Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);		JSONParser parser = new JSONParser();		JSONObject jsonObject = (JSONObject)parser.parse(reader);		responseStream.close();		String method = (String)jsonObject.get("method");		if (method != null) {			log.info("not null");			Template template = templateRepository.findById(templateId)					.orElseThrow(() -> new ResponseException(ResponseCode.NO_SUCH_TEMPLATE));			Payment payment = Payment.builder()					.paymentKey((String)jsonObject.get("paymentKey"))					.buyerId(userId)					.sellerId(sellerId)					.templateId(templateId)					.title(template.getTitle())					.thumbnail(template.getThumbnail())					.creatorName(template.getUser().getNickName())					.price(((Integer)jsonObject.get("balanceAmount")))					.approvedAt((String)jsonObject.get("approvedAt"))					.build();			paymentRepository.save(payment);			return payment.toDto();		} else {			log.info("is null");			String errorCode = (String)jsonObject.get("code");			String errorMessage = (String)jsonObject.get("message");			log.error("tosspayments errorCode: {}", errorCode);			log.error("tosspayments error: {}", errorMessage);			throw new ResponseException(ResponseCode.PAYMENT_ERROR);		}	}	public List<PaymentConfirmResDto> getPaidList(Long buyerId) {		List<Payment> paymentList = paymentRepository.findAllByBuyerId(buyerId);		List<PaymentConfirmResDto> result = new ArrayList<>();		paymentList.forEach(payment -> result.add(payment.toDto()));		return result;	}	public List<PaymentConfirmResDto> getSoldList(Long sellerId) {		List<Payment> paymentList = paymentRepository.findAllBySellerId(sellerId);		List<PaymentConfirmResDto> result = new ArrayList<>();		paymentList.forEach(payment -> result.add(payment.toDto()));		return result;	}}