package es.udc.rs.orders.model.customer;

import java.time.LocalDateTime;

public class Customer {

	private Long customerId;
	private String name;
	private String dni;
	private String address;
	private LocalDateTime creationDate;
	private String email;

	public Customer(String name, String dni, String address, String email) {
		super();
		this.name = name;
		this.dni = dni;
		this.address = address;
		this.email = email;
	}

	public Customer(Long customerId, String name, String dni, String address, LocalDateTime creationDate, String email) {
		this(name, dni, address, email);
		this.customerId = customerId;
		this.creationDate = creationDate;
	}

	public Customer(Customer customer) {
		this(customer.getCustomerId(), customer.getName(), customer.getDni(), customer.getAddress(), customer.getCreationDate(),
				customer.getEmail());
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", name=" + name + ", dni=" + dni + ", address=" + address
				+ ", creationDate=" + creationDate + ", email=" + email + "]";
	}

}