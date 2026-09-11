package es.udc.rs.orders.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderLine")
public class OrderLineDtoJaxb {

    private Long productId;
    private Long quantity;
    private Double price;

    public OrderLineDtoJaxb() {
    }

    public OrderLineDtoJaxb(Long productId, Long quantity, Double price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}