package com.e1i6.notionable.domain.usermailauth.service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

	private final Environment env;
	private final JavaMailSender javaMailSender;
	private final SpringTemplateEngine templateEngine;
	private static String senderEmail;

	private static final int AUTH_CODE_LENGTH = 5;
	private static final String AUTH_CODE_CHARACTER = "0123456789";

	private static String randomString;


	//실제 메일 전송
	public String sendAuthEmail(String toEmail) throws MessagingException, UnsupportedEncodingException {
		//메일전송에 필요한 정보 설정
		MimeMessage emailForm = createAuthMail(toEmail);
		//실제 메일 전송
		javaMailSender.send(emailForm);

		return randomString; //인증 코드 반환
	}

	public String sendPasswordEmail(String toEmail, String password)
			throws MessagingException, UnsupportedEncodingException {
		//메일전송에 필요한 정보 설정
		MimeMessage emailForm = createPasswordMail(toEmail, password);
		//실제 메일 전송
		javaMailSender.send(emailForm);

		return randomString; //인증 코드 반환
	}

	private MimeMessage createAuthMail(String mail) throws MessagingException, UnsupportedEncodingException {
		generateRandomCode();
		MimeMessage message = javaMailSender.createMimeMessage();
		senderEmail = env.getProperty("spring.mail.username");
		message.setFrom(new InternetAddress(senderEmail, "notionable"));
		message.setRecipients(MimeMessage.RecipientType.TO, mail);
		message.setSubject("[notionable] 이메일 인증");
		message.setText(setContext(randomString), "utf-8", "html");

		return message;

		/*
		String body = "";
		body += "<h3>" + "요청하신 인증코드입니다." + "</h3>";
		body += "<h1>" + randomString + "</h1>";
		body += "<h3>" + "감사합니다." + "</h3>";
		message.setText(body, "UTF-8", "html");*/

	}

	private MimeMessage createPasswordMail(String mail, String password)
			throws MessagingException, UnsupportedEncodingException {

		MimeMessage message = javaMailSender.createMimeMessage();
		senderEmail = env.getProperty("spring.mail.username");

		message.setFrom(new InternetAddress(senderEmail, "notionable"));
		message.setRecipients(MimeMessage.RecipientType.TO, mail);
		message.setSubject("[notionable] 이메일 인증");
		String body = "";
		body += "<h3>" + "임시로 설정된 비밀번호입니다. 이후에 비밀번호를 변경해 주세요." + "</h3>";
		body += "<h1>" + password + "</h1>";
		body += "<h3>" + "감사합니다." + "</h3>";
		message.setText(body, "UTF-8", "html");

		return message;
	}

	private static void generateRandomCode() {
		// 랜덤 코드 생성
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(AUTH_CODE_LENGTH);
		for (int i = 0; i < AUTH_CODE_LENGTH; i++) {
			int randomIndex = random.nextInt(AUTH_CODE_CHARACTER.length());
			char randomChar = AUTH_CODE_CHARACTER.charAt(randomIndex);
			sb.append(randomChar);
		}

		randomString = sb.toString();
	}

	//타임리프를 이용한 context 설정
	public String setContext(String code) {
		Context context = new Context();
		context.setVariable("code", code);

		return templateEngine.process("mail", context); //mail.html
	}
}
