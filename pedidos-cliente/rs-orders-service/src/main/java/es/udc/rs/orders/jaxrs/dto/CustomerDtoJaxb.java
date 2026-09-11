package es.udc.rs.orders.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "customer")
public class CustomerDtoJaxb {

    private Long customerId;
    private String name;
    private String dni;
    private String address;
    private String email;

    public CustomerDtoJaxb() {
    }

    public CustomerDtoJaxb(Long customerId, String name, String dni, String address, String email) {
        this.customerId = customerId;
        this.name = name;
        this.dni = dni;
        this.address = address;
        this.email = email;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getDni() {
        return dni;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}