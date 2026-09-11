package es.udc.rs.orders.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "invalidOrderStateException")
public class InvalidOrderStateExceptionDtoJaxb {

    private String message;

    public InvalidOrderStateExceptionDtoJaxb() {
    }

    public InvalidOrderStateExceptionDtoJaxb(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}