package es.udc.rs.orders.jaxrs.exceptions;

import es.udc.rs.orders.jaxrs.dto.InstanceNotFoundExceptionDtoJaxb;
import es.udc.rs.orders.model.exceptions.InstanceNotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InstanceNotFoundExceptionMapper implements ExceptionMapper<InstanceNotFoundException> {

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(InstanceNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new InstanceNotFoundExceptionDtoJaxb(exception.getMessage()))
                .type(ExceptionMapperUtils.getResponseMediaType(headers))
                .build();
    }
}