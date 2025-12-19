package com.example.billing.service;

import com.example.billing.entity.BillEntity;
import com.example.billing.exception.DuplicateAccountException;
import com.example.billing.exception.InvalidBillException;
import com.example.billing.model.input.BillXmlInput;
import com.example.billing.model.input.MeterXml;
import com.example.billing.model.output.BillJsonOutput;
import com.example.billing.model.output.MeterJson;
import com.example.billing.repository.BillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {

    private static final Logger successLog =
            LoggerFactory.getLogger("SUCCESS_LOG");

    private static final BigDecimal DELIVERY_RATE = new BigDecimal("0.05");
    private static final BigDecimal TAX_RATE = new BigDecimal("0.12");

    private final BillRepository billRepository;

    private final CurrencyConversionService currencyService;

    public BillServiceImpl(BillRepository repo,
                           CurrencyConversionService currencyService) {
        this.billRepository = repo;
        this.currencyService = currencyService;
    }

    @Override
    public BillJsonOutput convertXmlToJson(BillXmlInput input) {

        /* =========================
           1. BASIC VALIDATION
           ========================= */

        if (input == null) {
            throw new InvalidBillException("Input payload is null");
        }

        if (input.getAccountNo() == null || input.getAccountNo().isBlank()) {
            throw new InvalidBillException("Account number is mandatory");
        }

        if (input.getBillDate() == null || input.getBillDate().isBlank()) {
            throw new InvalidBillException("Bill date is mandatory");
        }

        if (input.getTotalAmount() == null || input.getTotalAmount().isBlank()) {
            throw new InvalidBillException("Total amount is mandatory");
        }

        if (input.getUsageKwh() == null || input.getUsageKwh().isBlank()) {
            throw new InvalidBillException("Usage KWH is mandatory");
        }

        /* =========================
           2. UNIQUE CONSTRAINT
           ========================= */

        if (billRepository.existsByAccountNo(input.getAccountNo())) {
            throw new DuplicateAccountException(
                    "Account number already exists: " + input.getAccountNo()
            );
        }

        try {
            /* =========================
               3. PARSING & CALCULATION
               ========================= */

            BigDecimal originalAmount =
                    new BigDecimal(input.getTotalAmount().replace("$", ""));

            BigDecimal totalDue =
                    currencyService.convertToINR(
                            originalAmount,
                            input.getCountryOfOrigin()
                    );

            BigDecimal usageKwh =
                    new BigDecimal(input.getUsageKwh());

            BigDecimal deliveryCharge =
                    usageKwh.multiply(DELIVERY_RATE);

            BigDecimal taxAmount =
                    totalDue.add(deliveryCharge).multiply(TAX_RATE);

            LocalDate billDate =
                    LocalDate.parse(
                            input.getBillDate(),
                            DateTimeFormatter.ofPattern("MM/dd/yyyy")
                    );

            /* =========================
               4. PERSIST DATA
               ========================= */

            BillEntity entity = new BillEntity();
            entity.setAccountNo(input.getAccountNo());
            entity.setBillDate(billDate);
            entity.setTotalDue(totalDue);
            entity.setDeliveryCharge(deliveryCharge);
            entity.setTaxAmount(taxAmount);

            billRepository.save(entity);

            /* =========================
               5. MAP METERS SAFELY
               ========================= */

            List<MeterJson> meters =
                    input.getMeters() == null ||
                            input.getMeters().getMeterList() == null
                            ? List.of()
                            : input.getMeters().getMeterList()
                            .stream()
                            .map(this::mapMeter)
                            .toList();

            /* =========================
               6. BUILD RESPONSE
               ========================= */

            BillJsonOutput output = new BillJsonOutput(
                    input.getAccountNo(),
                    billDate,
                    totalDue,
                    deliveryCharge,
                    taxAmount,
                    meters
            );

            /* =========================
               7. SUCCESS LOG
               ========================= */

            successLog.info(
                    "XML_TO_JSON_SUCCESS | Input={} | Output={}",
                    input,
                    output
            );

            return output;

        } catch (DateTimeParseException ex) {
            throw new InvalidBillException(
                    "Invalid billDate format. Expected MM/dd/yyyy", ex
            );
        } catch (NumberFormatException ex) {
            throw new InvalidBillException(
                    "Usage KWH must be numeric", ex
            );
        } catch (Exception ex) {
            throw new InvalidBillException(
                    "Invalid bill input data", ex
            );
        }
    }

    /* =========================
       HELPER METHOD
       ========================= */

    private MeterJson mapMeter(MeterXml meter) {

        LocalDate readDate =
                LocalDate.parse(
                        meter.getReadDate(),
                        DateTimeFormatter.ofPattern("MM/dd/yyyy")
                );

        return new MeterJson(
                meter.getNumber(),
                readDate
        );
    }
}