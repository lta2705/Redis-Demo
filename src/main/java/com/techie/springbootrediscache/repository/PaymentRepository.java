package com.techie.springbootrediscache.repository;

import com.techie.springbootrediscache.entity.PaymentRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentRequestEntity, Long> {}