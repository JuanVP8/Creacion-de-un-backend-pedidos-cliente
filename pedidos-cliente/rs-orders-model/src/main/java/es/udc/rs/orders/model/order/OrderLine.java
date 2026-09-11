package es.udc.rs.orders.model.order;

public class OrderLine {

	private Long productId;
	private Long quantity;
	private Double price;

	public OrderLine(Long productId, Long quantity, Double price) {
		super();
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
	}

	public OrderLine(OrderLine orderLine) {
		this.productId = orderLine.getProductId();
		this.quantity = orderLine.getQuantity();
		this.price = orderLine.getPrice();
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "OrderLine [productId=" + productId + ", quantity=" + quantity + ", price=" + price + "]";
	}

}
