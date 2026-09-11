package es.udc.rs.orders.jaxrs.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "order")
public class OrderDtoJaxb {

    private Long orderId;
    private Long customerId;
    private String orderDate;
    private List<OrderLineDtoJaxb> orderLines = new ArrayList<>();
    private String orderStatus;

    public OrderDtoJaxb() {
    }

    public OrderDtoJaxb(Long orderId, Long customerId, String orderDate,
            List<OrderLineDtoJaxb> orderLines, String orderStatus) {

        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.orderLines = orderLines;
        this.orderStatus = orderStatus;
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
    public List<OrderLineDtoJaxb> getOrderLines() {
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

    public void setOrderLines(List<OrderLineDtoJaxb> orderLines) {
        this.orderLines = orderLines;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}