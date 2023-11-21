package com.e1i6.notionable.domain.user.data.dto;

import com.e1i6.notionable.domain.user.entity.Role;
import com.e1i6.notionable.domain.user.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDto {

    private Long userId;
    private String email;
    private String password;
    private Integer userType;
    private Role role;
    private String nickName;
    private String profile;
    private String phoneNumber;

    public static UserDto toUserDto(User userEntity) {
        UserDto userDto = new UserDto();
        userDto.setUserId(userEntity.getUserId());
        userDto.setEmail(userEntity.getEmail());
        userDto.setPassword(userEntity.getPassword());
        userDto.setUserType(userEntity.getUserType());
        userDto.setRole(userEntity.getRole());
        userDto.setNickName(userEntity.getNickName());
        userDto.setProfile(userEntity.getProfile());
        userDto.setPhoneNumber(userEntity.getPhoneNumber());

        return userDto;
    }
}
