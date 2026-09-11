package es.udc.rs.orders.client.service.rest.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "status")
public class ClientStatusDtoJaxb {

    private String status;

    public ClientStatusDtoJaxb() {
    }

    public ClientStatusDtoJaxb(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}