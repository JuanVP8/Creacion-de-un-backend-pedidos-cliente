package es.udc.rs.orders.jaxrs.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import es.udc.rs.orders.jaxrs.dto.CustomerDtoJaxb;
import es.udc.rs.orders.jaxrs.dto.CustomerSummaryDtoJaxbList;
import es.udc.rs.orders.jaxrs.util.CustomerToCustomerDtoJaxbConversor;
import es.udc.rs.orders.model.customer.Customer;
import es.udc.rs.orders.model.exceptions.CustomerHasOrdersException;
import es.udc.rs.orders.model.exceptions.DuplicateInstanceException;
import es.udc.rs.orders.model.exceptions.InputValidationException;
import es.udc.rs.orders.model.exceptions.InstanceNotFoundException;
import es.udc.rs.orders.model.orderservice.OrderService;
import es.udc.rs.orders.model.orderservice.OrderServiceFactory;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.DELETE;
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

@Path("/customers")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class CustomerResource {

    private final OrderService orderService = OrderServiceFactory.getService();

    @POST
    public Response addCustomer(CustomerDtoJaxb customerDto, @Context UriInfo uriInfo)
            throws DuplicateInstanceException, InstanceNotFoundException, InputValidationException {

        validateCustomerDto(customerDto);

        Long customerId = orderService.addCustomer(
                customerDto.getName(),
                customerDto.getDni(),
                customerDto.getAddress(),
                customerDto.getEmail());

        Customer customer = orderService.findCustomerById(customerId);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(customerId.toString())
                .build();

        return Response.created(location)
                .entity(CustomerToCustomerDtoJaxbConversor.toCustomerDtoJaxb(customer))
                .build();
    }

    @PUT
    @Path("/{customerId}")
    public CustomerDtoJaxb updateCustomer(
            @PathParam("customerId") Long customerId,
            CustomerDtoJaxb customerDto)
            throws InstanceNotFoundException, DuplicateInstanceException, InputValidationException {

        if (customerDto == null) {
            throw new BadRequestException("Datos de cliente no proporcionados");
        }

        orderService.updateCustomer(
                customerId,
                customerDto.getName(),
                customerDto.getDni(),
                customerDto.getAddress(),
                customerDto.getEmail());

        Customer customer = orderService.findCustomerById(customerId);

        return CustomerToCustomerDtoJaxbConversor.toCustomerDtoJaxb(customer);
    }

    @DELETE
    @Path("/{customerId}")
    public Response removeCustomer(@PathParam("customerId") Long customerId)
            throws InstanceNotFoundException, CustomerHasOrdersException, InputValidationException {

        orderService.removeCustomer(customerId);

        return Response.noContent().build();
    }

    @GET
    @Path("/{customerId}")
    public CustomerDtoJaxb findCustomerById(@PathParam("customerId") Long customerId)
            throws InstanceNotFoundException, InputValidationException {

        Customer customer = orderService.findCustomerById(customerId);

        return CustomerToCustomerDtoJaxbConversor.toCustomerDtoJaxb(customer);
    }

    @GET
    @Path("/dni/{dni}")
    public CustomerDtoJaxb findCustomerByDni(@PathParam("dni") String dni)
            throws InstanceNotFoundException, InputValidationException {

        Customer customer = orderService.findCustomerByDni(dni);

        return CustomerToCustomerDtoJaxbConversor.toCustomerDtoJaxb(customer);
    }

    @GET
    public CustomerSummaryDtoJaxbList findCustomersByName(
            @QueryParam("keywords") String keywords,
            @DefaultValue("0") @QueryParam("from") Integer from,
            @DefaultValue("10") @QueryParam("max") Integer max)
            throws InputValidationException {

        validatePagination(from, max);

        List<Customer> customers = orderService.findCustomersByName(keywords);
        List<Customer> paginatedCustomers = paginate(customers, from, max);

        return new CustomerSummaryDtoJaxbList(
                CustomerToCustomerDtoJaxbConversor.toCustomerSummaryDtoJaxbList(paginatedCustomers));
    }

    private void validateCustomerDto(CustomerDtoJaxb customerDto) {
        if (customerDto == null) {
            throw new BadRequestException("Datos de cliente no proporcionados");
        }

        if (isBlank(customerDto.getName())
                || isBlank(customerDto.getDni())
                || isBlank(customerDto.getAddress())
                || isBlank(customerDto.getEmail())) {

            throw new BadRequestException("Campos obligatorios: name, dni, address, email");
        }
    }

    private void validatePagination(Integer from, Integer max) {
        if (from == null || from < 0) {
            throw new BadRequestException("El parámetro from debe ser mayor o igual que 0");
        }

        if (max == null || max <= 0) {
            throw new BadRequestException("El parámetro max debe ser mayor que 0");
        }
    }

    private static <T> List<T> paginate(List<T> list, int from, int max) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        int start = Math.min(from, list.size());
        int end = Math.min(start + max, list.size());

        return new ArrayList<>(list.subList(start, end));
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}