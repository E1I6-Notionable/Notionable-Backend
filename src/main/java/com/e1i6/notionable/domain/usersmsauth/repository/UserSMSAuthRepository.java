package com.e1i6.notionable.domain.usersmsauth.repository;

import com.e1i6.notionable.domain.usersmsauth.entity.UserSMSAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSMSAuthRepository extends JpaRepository<UserSMSAuthEntity, Long> {

    UserSMSAuthEntity findByPhoneNumber(String phoneNumber);
}
