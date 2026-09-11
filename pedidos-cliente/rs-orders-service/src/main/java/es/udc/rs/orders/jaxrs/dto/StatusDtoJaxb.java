package es.udc.rs.orders.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "status")
public class StatusDtoJaxb {

    private String status;

    public StatusDtoJaxb() {
    }

    public StatusDtoJaxb(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}