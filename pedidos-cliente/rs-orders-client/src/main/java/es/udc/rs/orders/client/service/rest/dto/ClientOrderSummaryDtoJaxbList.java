package es.udc.rs.orders.client.service.rest.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orders")
public class ClientOrderSummaryDtoJaxbList {

    private List<ClientOrderSummaryDtoJaxb> orders = new ArrayList<>();

    public ClientOrderSummaryDtoJaxbList() {
    }

    @XmlElement(name = "order")
    public List<ClientOrderSummaryDtoJaxb> getOrders() {
        return orders;
    }

    public void setOrders(List<ClientOrderSummaryDtoJaxb> orders) {
        this.orders = orders;
    }
}