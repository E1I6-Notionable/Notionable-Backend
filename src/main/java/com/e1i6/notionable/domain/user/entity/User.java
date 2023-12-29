package com.e1i6.notionable.domain.user.entity;

import javax.persistence.*;

import com.e1i6.notionable.domain.cart.Entity.Cart;
import com.e1i6.notionable.domain.user.data.dto.request.SocialLoginReqDto;
import com.e1i6.notionable.global.common.entity.BaseTimeEntity;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	@NotNull
	@Column(unique = true)
	private String email;
	private String password;
	private Integer userType;
	@Enumerated(EnumType.STRING)
	private Role role;
	private String nickName;
	private String profile;
	private String phoneNumber;

	public User(SocialLoginReqDto socialLoginReqDto) {
		this.email = socialLoginReqDto.getEmail();
		this.password = socialLoginReqDto.getPassword();
		this.userType = socialLoginReqDto.getUserType();
		this.nickName = socialLoginReqDto.getNickName();
		this.profile = socialLoginReqDto.getProfile();
		this.phoneNumber = socialLoginReqDto.getPhoneNumber();
		//role
	}

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Cart> cartItems;
}
