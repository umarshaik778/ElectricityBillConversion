package com.example.billing.handler;

import com.example.billing.exception.InvalidBillException;
import com.example.billing.model.output.BillJsonOutput;
import com.example.billing.exception.DuplicateAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger errorLog =
            LoggerFactory.getLogger("ERROR_LOG");

    /* =========================
       BUSINESS VALIDATION ERROR
       ========================= */

    @ExceptionHandler(InvalidBillException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidBill(InvalidBillException ex) {

        errorLog.error(
                "INVALID_BILL | {}",
                ex.getMessage(),
                ex
        );

        return Map.of(
                "error", ex.getMessage()
        );
    }

    /* =========================
       DUPLICATE ACCOUNT ERROR
       ========================= */

    @ExceptionHandler(DuplicateAccountException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDuplicateAccount(DuplicateAccountException ex) {

        errorLog.error(
                "DUPLICATE_ACCOUNT | {}",
                ex.getMessage(),
                ex
        );

        return Map.of(
                "error", ex.getMessage()
        );
    }

    /* =========================
       GENERIC / SAFETY-NET ERROR
       ========================= */

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGeneric(Exception ex) {

        errorLog.error(
                "UNEXPECTED_ERROR | {}",
                ex.getMessage(),
                ex
        );

        return Map.of(
                "error", "Internal server error"
        );
    }
}