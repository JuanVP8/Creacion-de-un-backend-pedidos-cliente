package es.udc.rs.orders.jaxrs.resources;

import java.net.URI;
import java.util.List;

import es.udc.rs.orders.jaxrs.dto.OrderDtoJaxb;
import es.udc.rs.orders.jaxrs.dto.OrderSummaryDtoJaxbList;
import es.udc.rs.orders.jaxrs.dto.StatusDtoJaxb;
import es.udc.rs.orders.jaxrs.util.OrderToOrderDtoJaxbConversor;
import es.udc.rs.orders.model.exceptions.InputValidationException;
import es.udc.rs.orders.model.exceptions.InstanceNotFoundException;
import es.udc.rs.orders.model.exceptions.InvalidOrderStateException;
import es.udc.rs.orders.model.order.Order;
import es.udc.rs.orders.model.order.OrderLine;
import es.udc.rs.orders.model.order.OrderStatus;
import es.udc.rs.orders.model.orderservice.OrderService;
import es.udc.rs.orders.model.orderservice.OrderServiceFactory;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/orders")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class OrderResource {

    private final OrderService orderService = OrderServiceFactory.getService();

    @POST
    public Response addOrder(OrderDtoJaxb orderDto, @Context UriInfo uriInfo)
            throws InstanceNotFoundException, InputValidationException {

        validateOrderDto(orderDto);

        List<OrderLine> orderLines =
                OrderToOrderDtoJaxbConversor.toOrderLineList(orderDto.getOrderLines());

        Long orderId = orderService.addOrder(orderDto.getCustomerId(), orderLines);

        Order order = orderService.findOrder(orderId);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(orderId.toString())
                .build();

        return Response.created(location)
                .entity(OrderToOrderDtoJaxbConversor.toOrderDtoJaxb(order))
                .build();
    }

    @GET
    @Path("/{orderId}")
    public OrderDtoJaxb findOrder(@PathParam("orderId") Long orderId)
            throws InstanceNotFoundException, InputValidationException {

        Order order = orderService.findOrder(orderId);

        return OrderToOrderDtoJaxbConversor.toOrderDtoJaxb(order);
    }

    @GET
    @Path("/customer/{customerId}")
    public OrderSummaryDtoJaxbList findOrdersByCustomer(
            @PathParam("customerId") Long customerId,
            @QueryParam("status") String status,
            @DefaultValue("0") @QueryParam("from") Integer from,
            @DefaultValue("10") @QueryParam("max") Integer max)
            throws InstanceNotFoundException, InputValidationException {

        validatePagination(from, max);

        OrderStatus orderStatus = parseOrderStatusAllowNull(status);

        List<Order> orders =
                orderService.findOrdersByCustomer(customerId, orderStatus, from, max);

        return new OrderSummaryDtoJaxbList(
                OrderToOrderDtoJaxbConversor.toOrderSummaryDtoJaxbList(orders));
    }

    @PUT
    @Path("/{orderId}/status")
    public OrderDtoJaxb changeOrderStatus(
            @PathParam("orderId") Long orderId,
            StatusDtoJaxb statusDto)
            throws InstanceNotFoundException, InvalidOrderStateException, InputValidationException {

        if (statusDto == null || statusDto.getStatus() == null || statusDto.getStatus().isBlank()) {
            throw new BadRequestException("Debe proporcionarse el nuevo estado del pedido");
        }

        OrderStatus newStatus = parseOrderStatus(statusDto.getStatus());

        Order updatedOrder = orderService.changeOrderStatus(orderId, newStatus);

        return OrderToOrderDtoJaxbConversor.toOrderDtoJaxb(updatedOrder);
    }

    private void validateOrderDto(OrderDtoJaxb orderDto) {
        if (orderDto == null) {
            throw new BadRequestException("Datos de pedido no proporcionados");
        }

        if (orderDto.getCustomerId() == null) {
            throw new BadRequestException("El pedido debe tener customerId");
        }

        if (orderDto.getOrderLines() == null || orderDto.getOrderLines().isEmpty()) {
            throw new BadRequestException("El pedido debe tener al menos una línea");
        }

        orderDto.getOrderLines().forEach(line -> {
            if (line.getProductId() == null || line.getQuantity() == null || line.getPrice() == null) {
                throw new BadRequestException("Cada línea debe tener productId, quantity y price");
            }

            if (line.getQuantity() <= 0) {
                throw new BadRequestException("La cantidad debe ser mayor que 0");
            }

            if (line.getPrice() <= 0) {
                throw new BadRequestException("El precio debe ser mayor que 0");
            }
        });
    }

    private void validatePagination(Integer from, Integer max) {
        if (from == null || from < 0) {
            throw new BadRequestException("El parámetro from debe ser mayor o igual que 0");
        }

        if (max == null || max <= 0) {
            throw new BadRequestException("El parámetro max debe ser mayor que 0");
        }
    }

    private OrderStatus parseOrderStatus(String status) {
        try {
            return OrderStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Estado de pedido no válido: " + status);
        }
    }

    private OrderStatus parseOrderStatusAllowNull(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }

        return parseOrderStatus(status);
    }
}