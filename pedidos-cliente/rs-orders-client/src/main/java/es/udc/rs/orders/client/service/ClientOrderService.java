package es.udc.rs.orders.client.service;

import java.util.List;

import es.udc.rs.orders.client.service.rest.dto.ClientCustomerDtoJaxb;
import es.udc.rs.orders.client.service.rest.dto.ClientOrderDtoJaxb;
import es.udc.rs.orders.client.service.rest.dto.ClientOrderSummaryDtoJaxb;

public interface ClientOrderService {

    ClientCustomerDtoJaxb addCustomer(String name, String dni, String address, String email);

    void removeCustomer(Long customerId);

    List<ClientOrderSummaryDtoJaxb> findOrdersByCustomer(
            Long customerId, String status, Integer from, Integer max);

    ClientOrderDtoJaxb changeOrderStatus(Long orderId, String status);
}