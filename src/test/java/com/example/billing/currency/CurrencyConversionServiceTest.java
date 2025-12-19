package com.example.billing.currency;


import com.example.billing.exception.InvalidBillException;
import com.example.billing.service.CurrencyConversionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CurrencyConversionServiceTest {

    @Autowired
    CurrencyConversionService service;

    @Test
    void shouldConvertUSDToINR() {
        BigDecimal result =
                service.convertToINR(
                        new BigDecimal("100"),
                        "us"
                );

        assertEquals(
                new BigDecimal("9000"),
                result
        );
    }

    @Test
    void shouldThrowErrorForUnknownCountry() {
        assertThrows(
                InvalidBillException.class,
                () -> service.convertToINR(
                        new BigDecimal("100"),
                        "xyz"
                )
        );
    }
}
