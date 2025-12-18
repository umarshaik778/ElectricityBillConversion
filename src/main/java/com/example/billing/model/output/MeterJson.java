package com.example.billing.model.output;

import java.time.LocalDate;

public class MeterJson {

    private String meterNumber;
    private LocalDate readDate;

    public MeterJson(String meterNumber, LocalDate readDate) {
        this.meterNumber = meterNumber;
        this.readDate = readDate;
    }

    public String getMeterNumber() { return meterNumber; }
    public LocalDate getReadDate() { return readDate; }

    @Override
    public String toString() {
        return "MeterJson{" +
                "meterNumber='" + meterNumber + '\'' +
                ", readDate=" + readDate +
                '}';
    }
}
