package es.udc.rs.orders.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "customerHasOrdersException")
public class CustomerHasOrdersExceptionDtoJaxb {

    private String message;

    public CustomerHasOrdersExceptionDtoJaxb() {
    }

    public CustomerHasOrdersExceptionDtoJaxb(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}