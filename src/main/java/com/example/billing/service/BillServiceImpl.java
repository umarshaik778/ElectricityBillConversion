package com.example.billing.service;

import com.example.billing.entity.BillEntity;
import com.example.billing.exception.DuplicateAccountException;
import com.example.billing.exception.InvalidBillException;
import com.example.billing.model.input.BillXmlInput;
import com.example.billing.model.input.MeterXml;
import com.example.billing.model.output.BillJsonOutput;
import com.example.billing.model.output.MeterJson;
import com.example.billing.repository.BillRepository;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;


@Service
public class BillServiceImpl implements BillService {

    private static final Logger successLog =
            LoggerFactory.getLogger("SUCCESS_LOG");

    private static final Logger requestLog =
            LoggerFactory.getLogger("REQUEST_LOG");


    private static final BigDecimal DELIVERY_RATE = new BigDecimal("0.05");
    private static final BigDecimal TAX_RATE = new BigDecimal("0.12");

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private final BillRepository billRepository;
    private final CurrencyConversionService currencyService;

    private final XmlMapper xmlMapper = new XmlMapper();

    private static final String BASE_DIRECTORY =
            "C:\\Users\\UMAR\\Documents\\ElectricityBillSamples";


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

        if (isBlank(input.getAccountNo())) {
            throw new InvalidBillException("Account number is mandatory");
        }

        if (isBlank(input.getBillDate())) {
            throw new InvalidBillException("Bill date is mandatory");
        }

        if (isBlank(input.getTotalAmount())) {
            throw new InvalidBillException("Total amount is mandatory");
        }

        if (isBlank(input.getUsageKwh())) {
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

        /* =========================
           3. PARSING & CALCULATION
           ========================= */

        BigDecimal originalAmount =
                parseBigDecimal(
                        input.getTotalAmount().replace("$", ""),
                        "totalAmount"
                );

        BigDecimal totalDue =
                currencyService.convertToINR(
                        originalAmount,
                        input.getCountryOfOrigin()
                );

        BigDecimal usageKwh =
                parseBigDecimal(
                        input.getUsageKwh(),
                        "usageKwh"
                );

        BigDecimal deliveryCharge =
                usageKwh.multiply(DELIVERY_RATE);

        BigDecimal taxAmount =
                totalDue.add(deliveryCharge).multiply(TAX_RATE);

        LocalDate billDate =
                parseDate(
                        input.getBillDate(),
                        "billDate"
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
    }

    /* =========================
       HELPER METHODS
       ========================= */

    private BigDecimal parseBigDecimal(String value, String fieldName) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException ex) {
            throw new InvalidBillException(
                    "Invalid numeric value for field: " + fieldName, ex
            );
        }
    }

    private LocalDate parseDate(String value, String fieldName) {
        try {
            return LocalDate.parse(value, DATE_FORMAT);
        } catch (DateTimeParseException ex) {
            throw new InvalidBillException(
                    "Invalid date format for field: " + fieldName +
                            ". Expected MM/dd/yyyy",
                    ex
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private MeterJson mapMeter(MeterXml meter) {

        LocalDate readDate =
                parseDate(
                        meter.getReadDate(),
                        "meter.readDate"
                );

        return new MeterJson(
                meter.getNumber(),
                readDate
        );
    }

    @Override
    public BillJsonOutput processBillFile(MultipartFile file) {

        try (InputStream is = file.getInputStream()) {

            BillXmlInput input =
                    xmlMapper.readValue(is, BillXmlInput.class);

            return convertXmlToJson(input);

        } catch (IOException ex) {
            throw new InvalidBillException(
                    "Unable to read uploaded file", ex
            );
        } catch (Exception ex) {
            throw new InvalidBillException(
                    "Failed to process uploaded bill file", ex
            );
        }
    }


    @Override
    public BillJsonOutput processBillFromServerPath(String fileName) {

        try {
            // 1️⃣ Build SAFE path
            Path basePath = Paths.get("C:\\Users\\UMAR\\Documents\\ElectricityBillSamples").normalize();

            Path filePath = basePath
                    .resolve(fileName)
                    .normalize();

            // 2️⃣ Security check
            if (!filePath.startsWith(basePath)) {
                throw new InvalidBillException("Invalid file path");
            }

            // 3️⃣ Existence check
            if (!Files.exists(filePath)) {
                throw new InvalidBillException(
                        "File not found: " + fileName
                );
            }

            requestLog.info(
                    "SERVER_FILE_REQUEST | FilePath={}",
                    filePath
            );

            // 4️⃣ Read & parse
            try (InputStream is = Files.newInputStream(filePath)) {

                BillXmlInput input =
                        xmlMapper.readValue(is, BillXmlInput.class);

                return convertXmlToJson(input);
            }

        } catch (InvalidBillException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InvalidBillException(
                    "Failed to process server-side file: " + fileName, ex
            );
        }
    }



}
