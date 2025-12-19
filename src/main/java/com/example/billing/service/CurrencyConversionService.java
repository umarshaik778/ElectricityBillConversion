package com.example.billing.service;

import com.example.billing.exception.InvalidBillException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyConversionService {

    private final CurrencyRateLoader rateLoader;

    public CurrencyConversionService(CurrencyRateLoader rateLoader) {
        this.rateLoader = rateLoader;
    }

    public BigDecimal convertToINR(BigDecimal amount, String countryCode) {

        BigDecimal rate = rateLoader.getRate(countryCode);

        if (rate == null) {
            throw new InvalidBillException(
                    "Unsupported countryOfOrigin: " + countryCode
            );
        }

        return amount.multiply(rate);
    }
}