package es.udc.rs.orders.jaxrs.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "customers")
public class CustomerSummaryDtoJaxbList {

    private List<CustomerSummaryDtoJaxb> customers = new ArrayList<>();

    public CustomerSummaryDtoJaxbList() {
    }

    public CustomerSummaryDtoJaxbList(List<CustomerSummaryDtoJaxb> customers) {
        this.customers = customers;
    }

    @XmlElement(name = "customer")
    public List<CustomerSummaryDtoJaxb> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerSummaryDtoJaxb> customers) {
        this.customers = customers;
    }
}