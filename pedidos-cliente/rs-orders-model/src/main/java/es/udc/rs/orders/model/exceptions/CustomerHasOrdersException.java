package es.udc.rs.orders.model.exceptions;

@SuppressWarnings("serial")
public class CustomerHasOrdersException extends Exception {

    private final Long customerId;

    public CustomerHasOrdersException(String message) {
        super(message);
        this.customerId = null;
    }

    public CustomerHasOrdersException(Long customerId) {
        super(String.format("El cliente con ID %d tiene pedidos asociados y no puede ser eliminado", customerId));
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }
}