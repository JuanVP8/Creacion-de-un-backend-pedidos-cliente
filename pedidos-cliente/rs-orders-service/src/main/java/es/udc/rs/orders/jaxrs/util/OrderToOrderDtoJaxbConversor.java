package es.udc.rs.orders.jaxrs.util;

import java.util.List;
import java.util.stream.Collectors;

import es.udc.rs.orders.jaxrs.dto.OrderDtoJaxb;
import es.udc.rs.orders.jaxrs.dto.OrderLineDtoJaxb;
import es.udc.rs.orders.jaxrs.dto.OrderSummaryDtoJaxb;
import es.udc.rs.orders.model.order.Order;
import es.udc.rs.orders.model.order.OrderLine;

public final class OrderToOrderDtoJaxbConversor {

    private OrderToOrderDtoJaxbConversor() {
    }

    public static OrderDtoJaxb toOrderDtoJaxb(Order order) {
        return new OrderDtoJaxb(
                order.getOrderId(),
                order.getCustomerId(),
                order.getOrderDate() == null ? null : order.getOrderDate().toString(),
                toOrderLineDtoJaxbList(order.getOrderLines()),
                order.getOrderStatus() == null ? null : order.getOrderStatus().name());
    }

    public static OrderSummaryDtoJaxb toOrderSummaryDtoJaxb(Order order) {
        return new OrderSummaryDtoJaxb(
                order.getOrderId(),
                order.getOrderDate() == null ? null : order.getOrderDate().toString(),
                calculateTotalPrice(order));
    }

    public static List<OrderSummaryDtoJaxb> toOrderSummaryDtoJaxbList(List<Order> orders) {
        return orders.stream()
                .map(OrderToOrderDtoJaxbConversor::toOrderSummaryDtoJaxb)
                .collect(Collectors.toList());
    }

    public static List<OrderLineDtoJaxb> toOrderLineDtoJaxbList(List<OrderLine> orderLines) {
        return orderLines.stream()
                .map(OrderToOrderDtoJaxbConversor::toOrderLineDtoJaxb)
                .collect(Collectors.toList());
    }

    public static OrderLineDtoJaxb toOrderLineDtoJaxb(OrderLine orderLine) {
        return new OrderLineDtoJaxb(
                orderLine.getProductId(),
                orderLine.getQuantity(),
                orderLine.getPrice());
    }

    public static List<OrderLine> toOrderLineList(List<OrderLineDtoJaxb> orderLineDtos) {
        return orderLineDtos.stream()
                .map(OrderToOrderDtoJaxbConversor::toOrderLine)
                .collect(Collectors.toList());
    }

    public static OrderLine toOrderLine(OrderLineDtoJaxb orderLineDto) {
        return new OrderLine(
                orderLineDto.getProductId(),
                orderLineDto.getQuantity(),
                orderLineDto.getPrice());
    }

    private static Double calculateTotalPrice(Order order) {
        double total = 0.0;

        if (order.getOrderLines() != null) {
            for (OrderLine line : order.getOrderLines()) {
                total += line.getQuantity() * line.getPrice();
            }
        }

        return total;
    }
}