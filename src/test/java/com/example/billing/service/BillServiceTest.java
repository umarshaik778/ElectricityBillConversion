package com.example.billing.service;

import com.example.billing.model.input.BillXmlInput;
import com.example.billing.model.output.BillJsonOutput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BillServiceTest {

    @Autowired
    BillService billService;

    @Test
    void shouldProcessBillWithCurrencyConversion() {

        BillXmlInput input = new BillXmlInput();
        input.setAccountNo("99999");
        input.setBillDate("12/16/2025");
        input.setTotalAmount("$100");
        input.setUsageKwh("10");
        input.setCountryOfOrigin("us");

        BillJsonOutput output =
                billService.convertXmlToJson(input);

        assertEquals(
                new BigDecimal("9000"),
                output.getTotalDue()
        );
    }
}
