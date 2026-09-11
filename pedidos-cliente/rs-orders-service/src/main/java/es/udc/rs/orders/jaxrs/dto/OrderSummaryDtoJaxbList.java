package es.udc.rs.orders.jaxrs.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orders")
public class OrderSummaryDtoJaxbList {

    private List<OrderSummaryDtoJaxb> orders = new ArrayList<>();

    public OrderSummaryDtoJaxbList() {
    }

    public OrderSummaryDtoJaxbList(List<OrderSummaryDtoJaxb> orders) {
        this.orders = orders;
    }

    @XmlElement(name = "order")
    public List<OrderSummaryDtoJaxb> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderSummaryDtoJaxb> orders) {
        this.orders = orders;
    }
}