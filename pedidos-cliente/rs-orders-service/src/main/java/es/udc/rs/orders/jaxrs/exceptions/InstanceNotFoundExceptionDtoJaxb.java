package es.udc.rs.orders.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "instanceNotFoundException")
public class InstanceNotFoundExceptionDtoJaxb {

    private String message;

    public InstanceNotFoundExceptionDtoJaxb() {
    }

    public InstanceNotFoundExceptionDtoJaxb(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}