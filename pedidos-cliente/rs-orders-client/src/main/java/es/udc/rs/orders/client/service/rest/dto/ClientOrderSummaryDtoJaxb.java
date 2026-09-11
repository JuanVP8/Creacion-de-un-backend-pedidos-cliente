package es.udc.rs.orders.client.service.rest.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderSummary")
public class ClientOrderSummaryDtoJaxb {

    private Long orderId;
    private String orderDate;
    private Double totalPrice;

    public ClientOrderSummaryDtoJaxb() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
