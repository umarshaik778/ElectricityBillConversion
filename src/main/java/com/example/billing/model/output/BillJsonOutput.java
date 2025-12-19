package com.example.billing.model.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillJsonOutput {

    private String accountNumber;
    private LocalDate billingDate;
    private BigDecimal totalDue;
    private BigDecimal deliveryCharge;
    private BigDecimal taxAmount;
    private List<MeterJson> meters;

}
