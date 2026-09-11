package es.udc.rs.orders.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "customerSummary")
public class CustomerSummaryDtoJaxb {

    private Long customerId;
    private String dni;
    private String name;

    public CustomerSummaryDtoJaxb() {
    }

    public CustomerSummaryDtoJaxb(Long customerId, String dni, String name) {
        this.customerId = customerId;
        this.dni = dni;
        this.name = name;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getDni() {
        return dni;
    }

    public String getName() {
        return name;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setName(String name) {
        this.name = name;
    }
}