package es.udc.rs.orders.client.service.rest.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "order")
public class ClientOrderDtoJaxb {

    private Long orderId;
    private Long customerId;
    private String orderDate;
    private List<ClientOrderLineDtoJaxb> orderLines = new ArrayList<>();
    private String orderStatus;

    public ClientOrderDtoJaxb() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    @XmlElement(name = "orderLine")
    public List<ClientOrderLineDtoJaxb> getOrderLines() {
        return orderLines;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderLines(List<ClientOrderLineDtoJaxb> orderLines) {
        this.orderLines = orderLines;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}