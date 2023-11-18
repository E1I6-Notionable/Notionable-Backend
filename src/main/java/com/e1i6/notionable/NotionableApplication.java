package com.e1i6.notionable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NotionableApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotionableApplication.class, args);
	}
}
