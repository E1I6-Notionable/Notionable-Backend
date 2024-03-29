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
	UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "unauthorized"),
	FORBIDDEN(403, HttpStatus.FORBIDDEN, "forbidden"),
	NOT_FOUND(404, HttpStatus.NOT_FOUND, "not found"),
	INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "internal server error"),

	SUCCESS_SEND_AUTH_CODE(2000, HttpStatus.OK, "인증 번호 전송에 성공하였습니다."),

	// user
	NO_SUCH_USER(1001, HttpStatus.BAD_REQUEST, "해당 유저가 없습니다."),
	WRONG_PASSWORD(1002, HttpStatus.BAD_REQUEST, "잘못된 비밀번호 입니다."),
	SAME_EMAIL_EXIST(1003, HttpStatus.BAD_REQUEST, "해당 이메일의 계정이 이미 존재합니다"),
	INVALID_USER(1004, HttpStatus.BAD_REQUEST, "해당 토큰값이 타당하지 않습니다"),

	// payment
	PAYMENT_ERROR(2001, HttpStatus.BAD_REQUEST, "payment fail"),
  
    // s3
	AWS_S3_UPLOAD_FAIL(3001, HttpStatus.INTERNAL_SERVER_ERROR, "업로드 요청 실패"),
	AWS_S3_WRONG_FILENAME(3002, HttpStatus.BAD_REQUEST, "잘못된 파일 정보"),

	// template
	NO_SUCH_TEMPLATE(4001, HttpStatus.BAD_REQUEST, "해당 템플릿을 찾을 수 없습니다."),
	NO_SUCH_CATEGORY(4002, HttpStatus.BAD_REQUEST, "해당 카테고리가 없습니다."),
	WRONG_CRITERIA(4003, HttpStatus.BAD_REQUEST, "잘못된 정렬 기준(가능 정렬 기준 목룍: createdAt, price)"),
	WRONG_CRITERIA_OPTION(4004, HttpStatus.BAD_REQUEST, "잘못된 정렬 방식(가능 정렬 방식: desc, asc)"),
	WRONG_TEMPLATE_TYPE(4005, HttpStatus.BAD_REQUEST, "잘못된 템플릿 타입(타입: free, paid)"),
	NO_IMAGES(4006, HttpStatus.BAD_REQUEST, "적어도 하나의 사진이 필요합니다"),

	// review
	NO_SUCH_REVIEW(5001, HttpStatus.BAD_REQUEST, "해당 리뷰를 찾을 수 없습니다."),
	NO_AUTHORIZATION(5002, HttpStatus.BAD_REQUEST, "해당 권한이 없습니다."),

	// cart
	DELETE_CART_ITEM_FAILED(6001, HttpStatus.BAD_REQUEST,"장바구니에서 해당 템플릿을 삭제하는데 실패하였습니다."),

	//community
	NO_SUCH_COMMUNITY(7001, HttpStatus.BAD_REQUEST, "해당 게시글을 찾을 수 없습니다."),
	NO_SUCH_COMMENT(7002, HttpStatus.BAD_REQUEST, "해당 댓글을 찾을 수 없습니다."),
	NO_SUCH_REPLY(7003, HttpStatus.BAD_REQUEST, "해당 대댓글을 찾을 수 없습니다."),
	NO_AUTHORITY(7004, HttpStatus.BAD_REQUEST, "권한이 없습니다."),

	// creator
	NO_SUCH_CREATOR(8001, HttpStatus.BAD_REQUEST, "크리에이터가 아닙니다.");

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
