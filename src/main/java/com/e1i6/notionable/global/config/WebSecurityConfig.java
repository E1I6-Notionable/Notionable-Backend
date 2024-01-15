package com.e1i6.notionable.global.config;

import com.e1i6.notionable.global.auth.exception.CustomAccessDeniedHandler;
import com.e1i6.notionable.global.auth.exception.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
		"/login/oauth2/social",
		"/find-password",

		// auth
		"/auth/not-secured",
		"/auth/denied",
		"/auth/reissue",

		// template
		"/template/recommend-free",
		"/template/recommend-paid",
		"/template/filter",
		"/template/detail/*",
		"/template/review-percent/*",

		// review
		"/template/review/list/*",

		"/test/*",
		"/favicon.ico"
	};

	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	private final CustomAccessDeniedHandler accessDeniedHandler;

	@Bean
	public SecurityFilterChain filterChain(
		HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
		http
			.httpBasic().disable()
			.cors().configurationSource(corsConfigurationSource())
			.and()

			.csrf().disable() // // token을 사용하는 방식이기 때문에 csrf를 disable
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않기 때문에 STATELESS로 설정

			.and()
			.formLogin().disable()

			.addFilterBefore(new RequestUrlLogFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests()
			.antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // for preflight cors
			.antMatchers(PERMITTED_URLS).permitAll()
			.anyRequest().authenticated()

			.and().exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint)
			.accessDeniedHandler(accessDeniedHandler);
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

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		config.setAllowCredentials(true);
		config.setAllowedOrigins(List.of("http://localhost:9000"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setExposedHeaders(List.of("*"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
