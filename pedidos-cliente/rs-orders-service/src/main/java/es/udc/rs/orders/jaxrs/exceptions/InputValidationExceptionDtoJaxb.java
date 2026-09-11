package es.udc.rs.orders.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "inputValidationException")
public class InputValidationExceptionDtoJaxb {

    private String message;

    public InputValidationExceptionDtoJaxb() {
    }

    public InputValidationExceptionDtoJaxb(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}