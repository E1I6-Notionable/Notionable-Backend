package com.e1i6.notionable.domain.payment.repository;import org.springframework.data.jpa.repository.JpaRepository;import com.e1i6.notionable.domain.payment.entity.Payment;import java.util.List;import java.util.Optional;public interface PaymentRepository extends JpaRepository<Payment, Long> {    List<Payment> findAllByBuyerId(Long buyerId);    List<Payment> findAllBySellerId(Long sellerId);    Optional<Payment> findByBuyerIdAndTemplateId(Long buyerId, Long templateId);    Integer countAllByBuyerId(Long buyerId);}