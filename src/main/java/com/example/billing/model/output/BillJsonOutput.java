package com.example.billing.model.output;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BillJsonOutput {

    private String accountNumber;
    private LocalDate billingDate;
    private BigDecimal totalDue;
    private BigDecimal deliveryCharge;
    private BigDecimal taxAmount;
    private List<MeterJson> meters;

    public BillJsonOutput(String accountNumber, LocalDate billingDate,
                          BigDecimal totalDue, BigDecimal deliveryCharge,
                          BigDecimal taxAmount, List<MeterJson> meters) {
        this.accountNumber = accountNumber;
        this.billingDate = billingDate;
        this.totalDue = totalDue;
        this.deliveryCharge = deliveryCharge;
        this.taxAmount = taxAmount;
        this.meters = meters;
    }

    public String getAccountNumber() { return accountNumber; }
    public LocalDate getBillingDate() { return billingDate; }
    public BigDecimal getTotalDue() { return totalDue; }
    public BigDecimal getDeliveryCharge() { return deliveryCharge; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public List<MeterJson> getMeters() { return meters; }

    @Override
    public String toString() {
        return "BillJsonOutput{" +
                "accountNumber='" + accountNumber + '\'' +
                ", billingDate=" + billingDate +
                ", totalDue=" + totalDue +
                ", deliveryCharge=" + deliveryCharge +
                ", taxAmount=" + taxAmount +
                ", meters=" + meters +
                '}';
    }
}
