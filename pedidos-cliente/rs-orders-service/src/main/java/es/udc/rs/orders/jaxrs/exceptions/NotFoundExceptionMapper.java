package es.udc.rs.orders.jaxrs.exceptions;

import es.udc.rs.orders.jaxrs.dto.InstanceNotFoundExceptionDtoJaxb;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(NotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new InstanceNotFoundExceptionDtoJaxb(exception.getMessage()))
                .type(ExceptionMapperUtils.getResponseMediaType(headers))
                .build();
    }
}