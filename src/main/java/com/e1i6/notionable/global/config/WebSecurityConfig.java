package com.e1i6.notionable.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.e1i6.notionable.global.auth.JwtAuthenticationFilter;
import com.e1i6.notionable.global.auth.JwtProvider;
import com.e1i6.notionable.global.filter.RequestUrlLogFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private static final String[] PERMITTED_URLS = {
			// user
			"/",
			"/login/**",
			"/signup/**",
		  "/auth/email",
			"/social/login",
			"/find-password",

			// template
			"/template/recommend-free",
			"/template/recommend-paid",
			"/template/filter",
			"/template/detail/*",
			"/template/review-percent/*",

			// review
			"/template/review/list/*",

			//community
			"/community/**",

			"/test/*",
			"/favicon.ico"


	};

	@Bean
	public SecurityFilterChain filterChain(
		HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
		http
			.csrf().disable() // // token을 사용하는 방식이기 때문에 csrf를 disable
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않기 때문에 STATELESS로 설정

			.and()
			.formLogin().disable()

			.addFilterBefore(new RequestUrlLogFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests()
			.antMatchers(PERMITTED_URLS).permitAll()
			.anyRequest().authenticated();
		return http.build();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(JwtProvider jwtProvider) {
		return new JwtAuthenticationFilter(jwtProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
