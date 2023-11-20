package com.e1i6.notionable.coolsms_auth.repository;

import com.e1i6.notionable.coolsms_auth.entity.CoolSMSEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoolSMSRepository extends JpaRepository<CoolSMSEntity, Long> {

    CoolSMSEntity findByPhoneNumber(String phoneNumber);
}
