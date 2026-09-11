package es.udc.rs.orders.model.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Order {

	private Long orderId;
	private Long customerId;
	private LocalDateTime orderDate;
	private List<OrderLine> orderLines;
	private OrderStatus orderStatus;

	public Order(Long customerId, List<OrderLine> orderLines, OrderStatus orderStatus) {
		super();
		this.customerId = customerId;
		this.orderLines = orderLines;
		this.orderStatus = orderStatus;
	}

	public Order(Long orderId, Long customerId, LocalDateTime orderDate, List<OrderLine> orderLines,
			OrderStatus orderStatus) {
		this(customerId, orderLines, orderStatus);
		this.orderId = orderId;
		this.orderDate = orderDate;
	}

	public Order(Order order) {
		this(order.getOrderId(), order.getCustomerId(), order.getOrderDate(),
				order.getOrderLines().stream().map(orderLine -> new OrderLine(orderLine)).collect(Collectors.toList()),
				order.getOrderStatus());
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public List<OrderLine> getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(List<OrderLine> orderLines) {
		this.orderLines = orderLines;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", customerId=" + customerId + ", orderDate=" + orderDate + ", orderLines="
				+ orderLines + ", orderStatus=" + orderStatus + "]";
	}

}