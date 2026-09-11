package es.udc.rs.orders.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "duplicateInstanceException")
public class DuplicateInstanceExceptionDtoJaxb {

    private String message;

    public DuplicateInstanceExceptionDtoJaxb() {
    }

    public DuplicateInstanceExceptionDtoJaxb(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}