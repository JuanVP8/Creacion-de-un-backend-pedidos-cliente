package es.udc.rs.orders.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderSummary")
public class OrderSummaryDtoJaxb {

    private Long orderId;
    private String orderDate;
    private Double totalPrice;

    public OrderSummaryDtoJaxb() {
    }

    public OrderSummaryDtoJaxb(Long orderId, String orderDate, Double totalPrice) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
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