package com.example.billing.controller;

import com.example.billing.model.input.BillXmlInput;
import com.example.billing.model.output.BillJsonOutput;
import com.example.billing.service.BillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bill")
public class BillController {

    private final BillService service;

    private static final Logger requestLog = LoggerFactory.getLogger("REQUEST_LOG");

    public BillController(BillService service) {
        this.service = service;
    }

    @PostMapping(
            value = "/xml-to-json",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public BillJsonOutput convert(@RequestBody BillXmlInput input) {

        requestLog.info(
                "INCOMING_REQUEST | Endpoint=/bill/xml-to-json | Input={}",
                input.toString()
        );

        return service.convertXmlToJson(input);
    }
}
