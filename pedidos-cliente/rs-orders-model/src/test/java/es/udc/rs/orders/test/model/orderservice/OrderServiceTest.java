package es.udc.rs.orders.test.model.orderservice;

import es.udc.rs.orders.model.customer.Customer;
import es.udc.rs.orders.model.exceptions.CustomerHasOrdersException;
import es.udc.rs.orders.model.exceptions.DuplicateInstanceException;
import es.udc.rs.orders.model.exceptions.InputValidationException;
import es.udc.rs.orders.model.exceptions.InstanceNotFoundException;
import es.udc.rs.orders.model.exceptions.InvalidOrderStateException;
import es.udc.rs.orders.model.order.Order;
import es.udc.rs.orders.model.order.OrderLine;
import es.udc.rs.orders.model.order.OrderStatus;
import es.udc.rs.orders.model.orderservice.MockOrderService;
import es.udc.rs.orders.model.orderservice.OrderService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {

    private OrderService service;

    @BeforeEach
    void setUp() {
        service = new MockOrderService();
    }

    /* ===================== Helpers ===================== */

    private Long addCustomer(String name, String dni)
            throws InputValidationException, DuplicateInstanceException {

        return service.addCustomer(name, dni, "C/ Progreso, 1", "test@example.com");
    }

    private List<OrderLine> sampleLines() {
        return Arrays.asList(
                new OrderLine(1L, 2L, 10.00),
                new OrderLine(2L, 1L, 5.50)
        );
    }

    /* ===================== Clientes ===================== */

    @Test
    void testAddAndFindCustomerByIdAndDni()
            throws InputValidationException, DuplicateInstanceException, InstanceNotFoundException {

        Long id = addCustomer("Ana Pérez", "00000000A");

        Customer byId = service.findCustomerById(id);
        assertNotNull(byId);
        assertEquals(id, byId.getCustomerId());
        assertEquals("Ana Pérez", byId.getName());
        assertEquals("00000000A", byId.getDni());
        assertNotNull(byId.getCreationDate());

        Customer byDni = service.findCustomerByDni("00000000A");
        assertNotNull(byDni);
        assertEquals(id, byDni.getCustomerId());
    }

    @Test
    void testAddCustomer_duplicateDni()
            throws InputValidationException, DuplicateInstanceException {

        addCustomer("Ana Pérez", "00000000A");

        assertThrows(DuplicateInstanceException.class,
                () -> addCustomer("Otra Persona", "00000000A"));
    }

    @Test
    void testAddCustomer_invalidData() {
        assertThrows(InputValidationException.class,
                () -> service.addCustomer("", "00000000A", "C/ Progreso, 1", "test@example.com"));

        assertThrows(InputValidationException.class,
                () -> service.addCustomer("Ana", "dni-mal", "C/ Progreso, 1", "test@example.com"));

        assertThrows(InputValidationException.class,
                () -> service.addCustomer("Ana", "00000000A", "", "test@example.com"));

        assertThrows(InputValidationException.class,
                () -> service.addCustomer("Ana", "00000000A", "C/ Progreso, 1", "correo-mal"));
    }

    @Test
    void testUpdateCustomer()
            throws InputValidationException, DuplicateInstanceException, InstanceNotFoundException {

        Long id = addCustomer("Juan López", "11111111B");

        service.updateCustomer(id, "Juan L. Actualizado", "11111111C", "C/ Nueva 2", "j@l.com");

        Customer c = service.findCustomerById(id);
        assertEquals("Juan L. Actualizado", c.getName());
        assertEquals("11111111C", c.getDni());
        assertEquals("C/ Nueva 2", c.getAddress());
        assertEquals("j@l.com", c.getEmail());
    }

    @Test
    void testRemoveCustomer_withoutOrders_ok()
            throws InputValidationException, DuplicateInstanceException,
            InstanceNotFoundException, CustomerHasOrdersException {

        Long id = addCustomer("Borrar Ok", "22222222D");

        service.removeCustomer(id);

        assertThrows(InstanceNotFoundException.class,
                () -> service.findCustomerById(id));

        assertThrows(InstanceNotFoundException.class,
                () -> service.findCustomerByDni("22222222D"));
    }

    @Test
    void testRemoveCustomer_withOrders_conflict()
            throws InputValidationException, DuplicateInstanceException,
            InstanceNotFoundException {

        Long id = addCustomer("Con Pedido", "33333333E");

        service.addOrder(id, sampleLines());

        assertThrows(CustomerHasOrdersException.class,
                () -> service.removeCustomer(id));

        assertNotNull(service.findCustomerById(id));
    }

    @Test
    void testFindCustomersByName_caseInsensitiveContains()
            throws InputValidationException, DuplicateInstanceException {

        Long a = addCustomer("María FernÁndez", "44444444F");
        Long b = addCustomer("Mario Lopez", "55555555G");
        Long c = addCustomer("Otro", "66666666H");

        List<Customer> res = service.findCustomersByName("mAr");

        assertEquals(2, res.size());
        assertTrue(res.stream().anyMatch(x -> x.getCustomerId().equals(a)));
        assertTrue(res.stream().anyMatch(x -> x.getCustomerId().equals(b)));
        assertFalse(res.stream().anyMatch(x -> x.getCustomerId().equals(c)));
    }

    @Test
    void testFindCustomersByName_emptyText()
            throws InputValidationException, DuplicateInstanceException {

        addCustomer("Ana Pérez", "00000000A");

        assertThrows(InputValidationException.class,
                () -> service.findCustomersByName(""));
    }

    /* ===================== Pedidos ===================== */

    @Test
    void testAddAndFindOrder()
            throws InstanceNotFoundException, InputValidationException, DuplicateInstanceException {

        Long cid = addCustomer("Cliente", "77777777J");

        Long oid = service.addOrder(cid, sampleLines());

        Order o = service.findOrder(oid);

        assertNotNull(o);
        assertEquals(oid, o.getOrderId());
        assertEquals(cid, o.getCustomerId());
        assertEquals(OrderStatus.PENDING, o.getOrderStatus());
        assertNotNull(o.getOrderDate());
        assertEquals(2, o.getOrderLines().size());
    }

    @Test
    void testAddOrder_invalidCustomer()
            throws InputValidationException {

        assertThrows(InstanceNotFoundException.class,
                () -> service.addOrder(999L, sampleLines()));
    }

    @Test
    void testAddOrder_invalidLines()
            throws InputValidationException, DuplicateInstanceException {

        Long cid = addCustomer("Cliente", "77777777J");

        assertThrows(InputValidationException.class,
                () -> service.addOrder(cid, null));

        assertThrows(InputValidationException.class,
                () -> service.addOrder(cid, List.of()));

        assertThrows(InputValidationException.class,
                () -> service.addOrder(cid, List.of(new OrderLine(0L, 1L, 10.0))));

        assertThrows(InputValidationException.class,
                () -> service.addOrder(cid, List.of(new OrderLine(1L, 0L, 10.0))));

        assertThrows(InputValidationException.class,
                () -> service.addOrder(cid, List.of(new OrderLine(1L, 1L, 0.0))));
    }

    @Test
    void testChangeOrderStatus_validTransitions()
            throws InputValidationException, DuplicateInstanceException,
            InstanceNotFoundException, InvalidOrderStateException {

        Long cid = addCustomer("Estado OK", "88888888K");
        Long oid = service.addOrder(cid, sampleLines());

        Order o1 = service.changeOrderStatus(oid, OrderStatus.PROCESSING);
        assertEquals(OrderStatus.PROCESSING, o1.getOrderStatus());

        Order o2 = service.changeOrderStatus(oid, OrderStatus.PROCESSED);
        assertEquals(OrderStatus.PROCESSED, o2.getOrderStatus());
    }

    @Test
    void testChangeOrderStatus_invalidTransition()
            throws InputValidationException, DuplicateInstanceException,
            InstanceNotFoundException {

        Long cid = addCustomer("Estado NOK", "99999999L");
        Long oid = service.addOrder(cid, sampleLines());

        assertThrows(InvalidOrderStateException.class,
                () -> service.changeOrderStatus(oid, OrderStatus.PROCESSED));

        Order order = service.findOrder(oid);
        assertEquals(OrderStatus.PENDING, order.getOrderStatus());
    }

    @Test
    void testChangeOrderStatus_nullStatus()
            throws InputValidationException, DuplicateInstanceException, InstanceNotFoundException {

        Long cid = addCustomer("Estado Null", "10101010P");
        Long oid = service.addOrder(cid, sampleLines());

        assertThrows(InputValidationException.class,
                () -> service.changeOrderStatus(oid, null));
    }

    /* ===================== Listados y paginación ===================== */

    @Test
    void testFindOrdersByCustomer_pagination()
            throws InputValidationException, DuplicateInstanceException, InstanceNotFoundException {

        Long cid = addCustomer("Pag", "12121212M");

        for (int i = 0; i < 12; i++) {
            service.addOrder(cid, sampleLines());
        }

        List<Order> p1 = service.findOrdersByCustomer(cid, null, 0, 5);
        assertEquals(5, p1.size());

        List<Order> p2 = service.findOrdersByCustomer(cid, null, 5, 5);
        assertEquals(5, p2.size());

        List<Order> p3 = service.findOrdersByCustomer(cid, null, 10, 5);
        assertEquals(2, p3.size());

        List<Order> p4 = service.findOrdersByCustomer(cid, null, 20, 5);
        assertTrue(p4.isEmpty());
    }

    @Test
    void testFindOrdersByCustomer_filterAndPaginate()
            throws InstanceNotFoundException, InputValidationException,
            DuplicateInstanceException, InvalidOrderStateException {

        Long cid = addCustomer("Filtros", "13131313N");

        service.addOrder(cid, sampleLines());
        Long o2 = service.addOrder(cid, sampleLines());
        Long o3 = service.addOrder(cid, sampleLines());

        service.changeOrderStatus(o2, OrderStatus.PROCESSING);

        service.changeOrderStatus(o3, OrderStatus.PROCESSING);
        service.changeOrderStatus(o3, OrderStatus.PROCESSED);

        List<Order> pending = service.findOrdersByCustomer(cid, OrderStatus.PENDING, 0, 10);
        assertEquals(1, pending.size());
        assertEquals(OrderStatus.PENDING, pending.get(0).getOrderStatus());

        List<Order> processing = service.findOrdersByCustomer(cid, OrderStatus.PROCESSING, 0, 10);
        assertEquals(1, processing.size());
        assertEquals(OrderStatus.PROCESSING, processing.get(0).getOrderStatus());

        List<Order> processed = service.findOrdersByCustomer(cid, OrderStatus.PROCESSED, 0, 10);
        assertEquals(1, processed.size());
        assertEquals(OrderStatus.PROCESSED, processed.get(0).getOrderStatus());

        List<Order> page1 = service.findOrdersByCustomer(cid, null, 0, 2);
        List<Order> page2 = service.findOrdersByCustomer(cid, null, 2, 2);

        assertEquals(2, page1.size());
        assertEquals(1, page2.size());
    }

    @Test
    void testFindOrdersByCustomer_invalidPagination()
            throws InputValidationException, DuplicateInstanceException {

        Long cid = addCustomer("Pag Error", "14141414Q");

        assertThrows(InputValidationException.class,
                () -> service.findOrdersByCustomer(cid, null, -1, 5));

        assertThrows(InputValidationException.class,
                () -> service.findOrdersByCustomer(cid, null, 0, 0));

        assertThrows(InputValidationException.class,
                () -> service.findOrdersByCustomer(cid, null, 0, null));
    }

    @AfterEach
    void tearDown() {
        ((MockOrderService) service).clear();
    }
}