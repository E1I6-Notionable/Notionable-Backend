package com.e1i6.notionable.global.common.response;

import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
	OK(200, HttpStatus.OK, "ok"),
	BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "bad request"),
	NOT_FOUND(404, HttpStatus.NOT_FOUND, "not found"),
	INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "internal server error"),

	SUCCESS_SEND_AUTH_CODE(2000, HttpStatus.OK, "인증 번호 전송에 성공하였습니다."),

	// user
	LOGIN_WITH_WRONG_PASSWORD(1001, HttpStatus.BAD_REQUEST, "wrong password"),

	// payment
	PAYMENT_ERROR(2001, HttpStatus.BAD_REQUEST, "payment fail"),
  
    // s3
	AWS_S3_UPLOAD_FAIL(3001, HttpStatus.INTERNAL_SERVER_ERROR, "업로드 요청 실패"),
	AWS_S3_WRONG_FILENAME(3002, HttpStatus.BAD_REQUEST, "잘못된 파일 정보"),

	// comment
	NO_SUCH_COMMENT(4001, HttpStatus.BAD_REQUEST, "해당 commentId를 찾을 수 없습니다."),
	NO_AUTHORIZATION(4002, HttpStatus.BAD_REQUEST, "해당 권한이 없습니다.");

	private final Integer code;
	private final HttpStatus httpStatus;
	private final String message;

	public String getMessage(Throwable e) {
		return this.getMessage(this.getMessage() + " - " + e.getMessage());
	}

	public String getMessage(String message) {
		return Optional.ofNullable(message)
			.filter(Predicate.not(String::isBlank))
			.orElse(this.getMessage());
	}
}
