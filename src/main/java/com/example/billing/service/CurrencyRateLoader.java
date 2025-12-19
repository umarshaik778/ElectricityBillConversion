package com.example.billing.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class CurrencyRateLoader {

    private final Map<String, BigDecimal> rates = new HashMap<>();

    @PostConstruct
    public void loadRates() {
        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     getClass().getClassLoader()
                                             .getResourceAsStream("currency-rates.txt")))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("-");
                rates.put(
                        parts[0].toLowerCase(),
                        new BigDecimal(parts[1])
                );
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to load currency rates file", ex);
        }
    }

    public BigDecimal getRate(String countryCode) {
        return rates.get(countryCode.toLowerCase());
    }
}
