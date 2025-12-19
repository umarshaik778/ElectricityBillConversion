package com.example.billing.model.input;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeterXml {

    @JacksonXmlProperty(localName = "number")
    private String number;

    @JacksonXmlProperty(localName = "readDate")
    private String readDate;

}
