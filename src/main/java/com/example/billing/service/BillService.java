package com.example.billing.service;

import com.example.billing.model.input.BillXmlInput;
import com.example.billing.model.output.BillJsonOutput;

public interface BillService {

    /**
     * Converts input XML bill data to JSON output,
     * applies business rules and persists the bill.
     *
     * @param input XML bill input
     * @return JSON bill output
     */
    BillJsonOutput convertXmlToJson(BillXmlInput input);
}
