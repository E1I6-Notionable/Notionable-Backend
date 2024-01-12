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

	public String sendNotionUrlEmail(String toEmail, String notionUrl)
			throws MessagingException, UnsupportedEncodingException {
		MimeMessage emailForm = createNotionUrlMail(toEmail, notionUrl);
		javaMailSender.send(emailForm);

		return "send mail success";
	}

	private MimeMessage createAuthMail(String mail) throws MessagingException, UnsupportedEncodingException {
		generateRandomCode();
		MimeMessage message = javaMailSender.createMimeMessage();
		senderEmail = env.getProperty("spring.mail.username");
		message.setFrom(new InternetAddress(senderEmail, "notionable"));
		message.setRecipients(MimeMessage.RecipientType.TO, mail);
		message.setSubject("[notionable] 이메일 인증");

		Context context = new Context();
		context.setVariable("code", randomString);
		message.setText(templateEngine.process("mail", context), "utf-8", "html");

		return message;
	}

	private MimeMessage createPasswordMail(String mail, String password)
			throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		senderEmail = env.getProperty("spring.mail.username");
		message.setFrom(new InternetAddress(senderEmail, "notionable"));
		message.setRecipients(MimeMessage.RecipientType.TO, mail);
		message.setSubject("[notionable] 비밀번호 찾기 안내");

		Context context = new Context();
		context.setVariable("password", password);
		message.setText(templateEngine.process("password", context), "utf-8", "html");

		return message;
	}

	private MimeMessage createNotionUrlMail(String mail, String notionUrl)
			throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		senderEmail = env.getProperty("spring.mail.username");
		message.setFrom(new InternetAddress(senderEmail, "notionable"));
		message.setRecipients(MimeMessage.RecipientType.TO, mail);
		message.setSubject("[notionable] 요청하신 템플릿 링크를 보내드립니다");

		Context context = new Context();
		context.setVariable("url", notionUrl);
		message.setText(templateEngine.process("notion-url", context), "utf-8", "html");

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
}
