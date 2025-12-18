package com.example.billing.model.input;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class MetersXml {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "meter")
    private List<MeterXml> meterList;

    public List<MeterXml> getMeterList() {
        return meterList;
    }

    @Override
    public String toString() {
        return "MetersXml{" +
                "meterList=" + meterList +
                '}';
    }
}
