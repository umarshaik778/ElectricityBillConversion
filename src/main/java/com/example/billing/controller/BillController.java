package com.example.billing.controller;

import com.example.billing.model.input.BillXmlInput;
import com.example.billing.model.output.BillJsonOutput;
import com.example.billing.service.BillService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bill")
public class BillController {

    private final BillService service;

    public BillController(BillService service) {
        this.service = service;
    }

    @PostMapping(
            value = "/xml-to-json",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public BillJsonOutput convert(@RequestBody BillXmlInput input) {
        return service.convertXmlToJson(input);
    }
}
