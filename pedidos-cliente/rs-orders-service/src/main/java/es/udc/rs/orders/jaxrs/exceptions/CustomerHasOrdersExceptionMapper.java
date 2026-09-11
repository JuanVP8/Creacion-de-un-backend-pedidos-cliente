package es.udc.rs.orders.jaxrs.exceptions;

import es.udc.rs.orders.jaxrs.dto.CustomerHasOrdersExceptionDtoJaxb;
import es.udc.rs.orders.model.exceptions.CustomerHasOrdersException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CustomerHasOrdersExceptionMapper implements ExceptionMapper<CustomerHasOrdersException> {

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(CustomerHasOrdersException exception) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new CustomerHasOrdersExceptionDtoJaxb(exception.getMessage()))
                .type(ExceptionMapperUtils.getResponseMediaType(headers))
                .build();
    }
}