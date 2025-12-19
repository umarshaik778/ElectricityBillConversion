package com.example.billing.model.input;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import com.example.billing.model.input.MetersXml;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "bill")
public class BillXmlInput {

    @JacksonXmlProperty(localName = "accountNo")
    private String accountNo;

    @JacksonXmlProperty(localName = "billDate")
    private String billDate;

    @JacksonXmlProperty(localName = "totalAmount")
    private String totalAmount;

    @JacksonXmlProperty(localName = "usageKwh")
    private String usageKwh;

    @JacksonXmlProperty(localName = "countryOfOrigin")
    private String countryOfOrigin;

    @JacksonXmlProperty(localName = "meters")
    private MetersXml meters;

}
