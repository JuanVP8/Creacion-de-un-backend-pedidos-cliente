package es.udc.rs.orders.model.orderservice;

import java.util.List;

import es.udc.rs.orders.model.customer.Customer;
import es.udc.rs.orders.model.exceptions.CustomerHasOrdersException;
import es.udc.rs.orders.model.exceptions.DuplicateInstanceException;
import es.udc.rs.orders.model.exceptions.InputValidationException;
import es.udc.rs.orders.model.exceptions.InstanceNotFoundException;
import es.udc.rs.orders.model.exceptions.InvalidOrderStateException;
import es.udc.rs.orders.model.order.Order;
import es.udc.rs.orders.model.order.OrderLine;
import es.udc.rs.orders.model.order.OrderStatus;

public interface OrderService {

    Long addCustomer(String name, String dni, String address, String email)
            throws DuplicateInstanceException, InputValidationException;

    void updateCustomer(Long customerId, String name, String dni, String address, String email)
            throws InstanceNotFoundException, DuplicateInstanceException, InputValidationException;

    void removeCustomer(Long customerId)
            throws InstanceNotFoundException, CustomerHasOrdersException, InputValidationException;

    Customer findCustomerById(Long customerId)
            throws InstanceNotFoundException, InputValidationException;

    Customer findCustomerByDni(String dni)
            throws InstanceNotFoundException, InputValidationException;

    List<Customer> findCustomersByName(String text)
            throws InputValidationException;

    Long addOrder(Long customerId, List<OrderLine> lines)
            throws InstanceNotFoundException, InputValidationException;

    Order findOrder(Long orderId)
            throws InstanceNotFoundException, InputValidationException;

    Order changeOrderStatus(Long orderId, OrderStatus newStatus)
            throws InstanceNotFoundException, InvalidOrderStateException, InputValidationException;

    List<Order> findOrdersByCustomer(Long customerId, OrderStatus status, Integer from, Integer max)
            throws InstanceNotFoundException, InputValidationException;
}