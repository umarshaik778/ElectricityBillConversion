package com.example.billing.controller;

import com.example.billing.exception.InvalidBillException;
import com.example.billing.model.input.BillXmlInput;
import com.example.billing.model.output.BillJsonOutput;
import com.example.billing.model.request.FileNameRequest;
import com.example.billing.service.BillService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/bill")
public class BillFileController {

    private static final Logger requestLog =
            LoggerFactory.getLogger("REQUEST_LOG");

    private final BillService billService;

    public BillFileController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping(
            value = "/process-file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public BillJsonOutput processBillFile(
            @RequestPart("file") MultipartFile file
    ) {

        if (file.isEmpty()) {
            throw new InvalidBillException("Uploaded file is empty");
        }

        requestLog.info(
                "FILE_UPLOAD_REQUEST | FileName={}",
                file.getOriginalFilename()
        );

        return billService.processBillFile(file);
    }

    @PostMapping("/process-by-filename")
    public BillJsonOutput processByFileName(
            @RequestBody FileNameRequest request) {

        if (request.getFileName() == null || request.getFileName().isBlank()) {
            throw new InvalidBillException("File name is mandatory");
        }

        return billService.processBillFromServerPath(request.getFileName());
    }
}