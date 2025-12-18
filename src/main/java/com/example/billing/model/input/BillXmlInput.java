package com.example.billing.model.input;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import com.example.billing.model.input.MetersXml;

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

    @JacksonXmlProperty(localName = "meters")
    private MetersXml meters;

    public String getAccountNo() { return accountNo; }
    public String getBillDate() { return billDate; }
    public String getTotalAmount() { return totalAmount; }
    public String getUsageKwh() { return usageKwh; }
    public MetersXml getMeters() { return meters; }

    @Override
    public String toString() {
        return "BillXmlInput{" +
                "accountNo='" + accountNo + '\'' +
                ", billDate='" + billDate + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", usageKwh='" + usageKwh + '\'' +
                ", meters=" + meters +
                '}';
    }
}
