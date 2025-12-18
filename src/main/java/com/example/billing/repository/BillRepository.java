package com.example.billing.repository;

import com.example.billing.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillRepository extends JpaRepository<BillEntity, Long> {

    Optional<BillEntity> findByAccountNo(String accountNo);

    boolean existsByAccountNo(String accountNo);
}
