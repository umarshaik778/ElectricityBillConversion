package com.example.billing.model.input;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class MeterXml {

    @JacksonXmlProperty(localName = "number")
    private String number;

    @JacksonXmlProperty(localName = "readDate")
    private String readDate;

    public String getNumber() { return number; }
    public String getReadDate() { return readDate; }

    @Override
    public String toString() {
        return "MeterXml{" +
                "number='" + number + '\'' +
                ", readDate='" + readDate + '\'' +
                '}';
    }
}
