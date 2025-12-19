package com.example.billing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "bill",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "account_no")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "account_no", nullable = false, unique = true)
    private String accountNo;
    private LocalDate billDate;
    private BigDecimal totalDue;
    private BigDecimal deliveryCharge;
    private BigDecimal taxAmount;

}
