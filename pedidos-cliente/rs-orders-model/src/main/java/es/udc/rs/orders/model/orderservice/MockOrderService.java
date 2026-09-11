package es.udc.rs.orders.model.orderservice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import es.udc.rs.orders.model.customer.Customer;
import es.udc.rs.orders.model.exceptions.CustomerHasOrdersException;
import es.udc.rs.orders.model.exceptions.DuplicateInstanceException;
import es.udc.rs.orders.model.exceptions.InputValidationException;
import es.udc.rs.orders.model.exceptions.InstanceNotFoundException;
import es.udc.rs.orders.model.exceptions.InvalidOrderStateException;
import es.udc.rs.orders.model.order.Order;
import es.udc.rs.orders.model.order.OrderLine;
import es.udc.rs.orders.model.order.OrderStatus;

public class MockOrderService implements OrderService {

    private final Map<Long, Customer> customersMap = new LinkedHashMap<>();
    private final Map<Long, Order> ordersMap = new LinkedHashMap<>();
    private final Map<Long, List<Order>> ordersByCustomerMap = new LinkedHashMap<>();
    private final Map<String, Long> customerIdByDni = new LinkedHashMap<>();

    private long lastCustomerId = 0;
    private long lastOrderId = 0;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private synchronized long getNextCustomerId() {
        return ++lastCustomerId;
    }

    private synchronized long getNextOrderId() {
        return ++lastOrderId;
    }

    @Override
    public Long addCustomer(String name, String dni, String address, String email)
            throws DuplicateInstanceException, InputValidationException {

        validateCustomerData(name, dni, address, email);

        String normalizedDni = normalizeDni(dni);

        if (customerIdByDni.containsKey(normalizedDni)) {
            throw new DuplicateInstanceException(normalizedDni, Customer.class, "dni");
        }

        Long customerId = getNextCustomerId();

        Customer customer = new Customer(
                customerId,
                name.trim(),
                normalizedDni,
                address.trim(),
                LocalDateTime.now(),
                email.trim()
        );

        customersMap.put(customerId, customer);
        customerIdByDni.put(normalizedDni, customerId);

        return customerId;
    }

    @Override
    public void updateCustomer(Long customerId, String name, String dni, String address, String email)
            throws InstanceNotFoundException, DuplicateInstanceException, InputValidationException {

        if (customerId == null || customerId <= 0) {
            throw new InputValidationException("Invalid customer id");
        }

        Customer customer = customersMap.get(customerId);

        if (customer == null) {
            throw new InstanceNotFoundException(customerId, Customer.class);
        }

        validateCustomerData(name, dni, address, email);

        String normalizedDni = normalizeDni(dni);
        Long existingCustomerId = customerIdByDni.get(normalizedDni);

        if (existingCustomerId != null && !existingCustomerId.equals(customerId)) {
            throw new DuplicateInstanceException(normalizedDni, Customer.class, "dni");
        }

        customerIdByDni.remove(customer.getDni());

        customer.setName(name.trim());
        customer.setDni(normalizedDni);
        customer.setAddress(address.trim());
        customer.setEmail(email.trim());

        customerIdByDni.put(normalizedDni, customerId);
    }

    @Override
    public void removeCustomer(Long customerId)
            throws InstanceNotFoundException, CustomerHasOrdersException, InputValidationException {

        if (customerId == null || customerId <= 0) {
            throw new InputValidationException("Invalid customer id");
        }

        Customer customer = customersMap.get(customerId);

        if (customer == null) {
            throw new InstanceNotFoundException(customerId, Customer.class);
        }

        List<Order> orders = ordersByCustomerMap.get(customerId);

        if (orders != null && !orders.isEmpty()) {
            throw new CustomerHasOrdersException(customerId);
        }

        customersMap.remove(customerId);
        customerIdByDni.remove(customer.getDni());
        ordersByCustomerMap.remove(customerId);
    }

    @Override
    public Customer findCustomerById(Long customerId)
            throws InstanceNotFoundException, InputValidationException {

        if (customerId == null || customerId <= 0) {
            throw new InputValidationException("Invalid customer id");
        }

        Customer customer = customersMap.get(customerId);

        if (customer == null) {
            throw new InstanceNotFoundException(customerId, Customer.class);
        }

        return new Customer(customer);
    }

    @Override
    public Customer findCustomerByDni(String dni)
            throws InstanceNotFoundException, InputValidationException {

        if (isBlank(dni)) {
            throw new InputValidationException("Invalid DNI");
        }

        String normalizedDni = normalizeDni(dni);
        Long customerId = customerIdByDni.get(normalizedDni);

        if (customerId == null) {
            throw new InstanceNotFoundException(normalizedDni, Customer.class);
        }

        return new Customer(customersMap.get(customerId));
    }

    @Override
    public List<Customer> findCustomersByName(String text)
            throws InputValidationException {

        if (isBlank(text)) {
            throw new InputValidationException("Search text cannot be empty");
        }

        String normalizedText = text.trim().toLowerCase();
        List<Customer> result = new ArrayList<>();

        for (Customer customer : customersMap.values()) {
            if (customer.getName().toLowerCase().contains(normalizedText)) {
                result.add(new Customer(customer));
            }
        }

        return result;
    }

    @Override
    public Long addOrder(Long customerId, List<OrderLine> lines)
            throws InstanceNotFoundException, InputValidationException {

        if (customerId == null || customerId <= 0) {
            throw new InputValidationException("Invalid customer id");
        }

        if (!customersMap.containsKey(customerId)) {
            throw new InstanceNotFoundException(customerId, Customer.class);
        }

        validateOrderLines(lines);

        Long orderId = getNextOrderId();

        Order order = new Order(
                orderId,
                customerId,
                LocalDateTime.now(),
                lines,
                OrderStatus.PENDING
        );

        ordersMap.put(orderId, order);
        ordersByCustomerMap.computeIfAbsent(customerId, k -> new LinkedList<>()).add(order);

        return orderId;
    }

    @Override
    public Order findOrder(Long orderId)
            throws InstanceNotFoundException, InputValidationException {

        if (orderId == null || orderId <= 0) {
            throw new InputValidationException("Invalid order id");
        }

        Order order = ordersMap.get(orderId);

        if (order == null) {
            throw new InstanceNotFoundException(orderId, Order.class);
        }

        return new Order(order);
    }

    @Override
    public Order changeOrderStatus(Long orderId, OrderStatus newStatus)
            throws InstanceNotFoundException, InvalidOrderStateException, InputValidationException {

        if (orderId == null || orderId <= 0) {
            throw new InputValidationException("Invalid order id");
        }

        if (newStatus == null) {
            throw new InputValidationException("New order status cannot be null");
        }

        Order order = ordersMap.get(orderId);

        if (order == null) {
            throw new InstanceNotFoundException(orderId, Order.class);
        }

        OrderStatus currentStatus = order.getOrderStatus();

        boolean validTransition =
                currentStatus == OrderStatus.PENDING && newStatus == OrderStatus.PROCESSING ||
                currentStatus == OrderStatus.PROCESSING && newStatus == OrderStatus.PROCESSED;

        if (!validTransition) {
            throw new InvalidOrderStateException(orderId, currentStatus, newStatus);
        }

        order.setOrderStatus(newStatus);

        return new Order(order);
    }

    @Override
    public List<Order> findOrdersByCustomer(Long customerId, OrderStatus status, Integer from, Integer max)
            throws InstanceNotFoundException, InputValidationException {

        if (customerId == null || customerId <= 0) {
            throw new InputValidationException("Invalid customer id");
        }

        if (!customersMap.containsKey(customerId)) {
            throw new InstanceNotFoundException(customerId, Customer.class);
        }

        validatePagination(from, max);

        List<Order> orders = ordersByCustomerMap.getOrDefault(customerId, Collections.emptyList());
        List<Order> filteredOrders = new ArrayList<>();

        for (Order order : orders) {
            if (status == null || order.getOrderStatus() == status) {
                filteredOrders.add(new Order(order));
            }
        }

        return paginate(filteredOrders, from, max);
    }

    private static void validateCustomerData(String name, String dni, String address, String email)
            throws InputValidationException {

        if (isBlank(name)) {
            throw new InputValidationException("Name cannot be empty");
        }

        if (isBlank(dni)) {
            throw new InputValidationException("DNI cannot be empty");
        }

        String normalizedDni = normalizeDni(dni);

        if (!normalizedDni.matches("^[0-9]{8}[A-Z]$")) {
            throw new InputValidationException("Invalid DNI format");
        }

        if (isBlank(address)) {
            throw new InputValidationException("Address cannot be empty");
        }

        if (isBlank(email)) {
            throw new InputValidationException("Email cannot be empty");
        }

        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new InputValidationException("Invalid email format");
        }
    }

    private static void validateOrderLines(List<OrderLine> lines)
            throws InputValidationException {

        if (lines == null || lines.isEmpty()) {
            throw new InputValidationException("Order must contain at least one order line");
        }

        for (OrderLine line : lines) {
            if (line == null) {
                throw new InputValidationException("Order line cannot be null");
            }

            if (line.getProductId() == null || line.getProductId() <= 0) {
                throw new InputValidationException("Invalid product id");
            }

            if (line.getQuantity() == null || line.getQuantity() <= 0) {
                throw new InputValidationException("Invalid quantity");
            }

            if (line.getPrice() == null || line.getPrice() <= 0) {
                throw new InputValidationException("Invalid price");
            }
        }
    }

    private static void validatePagination(Integer from, Integer max)
            throws InputValidationException {

        if (from == null && max == null) {
            return;
        }

        if (from == null || max == null) {
            throw new InputValidationException("Both from and max must be provided");
        }

        if (from < 0) {
            throw new InputValidationException("From index cannot be negative");
        }

        if (max <= 0) {
            throw new InputValidationException("Max results must be greater than zero");
        }
    }

    private static <T> List<T> paginate(List<T> list, Integer from, Integer max) {
        if (from == null && max == null) {
            return new ArrayList<>(list);
        }

        if (from >= list.size()) {
            return new ArrayList<>();
        }

        int toIndex = Math.min(from + max, list.size());

        return new ArrayList<>(list.subList(from, toIndex));
    }

    private static String normalizeDni(String dni) {
        return dni.trim().replaceAll("\\s+", "").toUpperCase();
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public void clear() {
        customersMap.clear();
        ordersMap.clear();
        ordersByCustomerMap.clear();
        customerIdByDni.clear();
        lastCustomerId = 0;
        lastOrderId = 0;
    }
}