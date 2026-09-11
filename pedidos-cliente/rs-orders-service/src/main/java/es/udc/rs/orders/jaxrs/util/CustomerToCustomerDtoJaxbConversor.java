package es.udc.rs.orders.jaxrs.util;

import java.util.List;
import java.util.stream.Collectors;

import es.udc.rs.orders.jaxrs.dto.CustomerDtoJaxb;
import es.udc.rs.orders.jaxrs.dto.CustomerSummaryDtoJaxb;
import es.udc.rs.orders.model.customer.Customer;

public final class CustomerToCustomerDtoJaxbConversor {

    private CustomerToCustomerDtoJaxbConversor() {
    }

    public static CustomerDtoJaxb toCustomerDtoJaxb(Customer customer) {
        return new CustomerDtoJaxb(
                customer.getCustomerId(),
                customer.getName(),
                customer.getDni(),
                customer.getAddress(),
                customer.getEmail());
    }

    public static CustomerSummaryDtoJaxb toCustomerSummaryDtoJaxb(Customer customer) {
        return new CustomerSummaryDtoJaxb(
                customer.getCustomerId(),
                customer.getDni(),
                customer.getName());
    }

    public static List<CustomerSummaryDtoJaxb> toCustomerSummaryDtoJaxbList(List<Customer> customers) {
        return customers.stream()
                .map(CustomerToCustomerDtoJaxbConversor::toCustomerSummaryDtoJaxb)
                .collect(Collectors.toList());
    }
}