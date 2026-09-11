package es.udc.rs.orders.model.exceptions;

import es.udc.rs.orders.model.order.OrderStatus;

@SuppressWarnings("serial")
public class InvalidOrderStateException extends Exception {

    private final Long orderId;
    private final OrderStatus currentStatus;
    private final OrderStatus attemptedStatus;

    public InvalidOrderStateException(String message) {
        super(message);
        this.orderId = null;
        this.currentStatus = null;
        this.attemptedStatus = null;
    }

    public InvalidOrderStateException(Long orderId, OrderStatus currentStatus, OrderStatus attemptedStatus) {
        super(String.format("Transición inválida para pedido %d: %s -> %s",
                orderId, currentStatus, attemptedStatus));
        this.orderId = orderId;
        this.currentStatus = currentStatus;
        this.attemptedStatus = attemptedStatus;
    }

    public Long getOrderId() {
        return orderId;
    }

    public OrderStatus getCurrentStatus() {
        return currentStatus;
    }

    public OrderStatus getAttemptedStatus() {
        return attemptedStatus;
    }
}