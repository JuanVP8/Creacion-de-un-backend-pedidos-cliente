package es.udc.rs.orders.jaxrs.exceptions;

import es.udc.rs.orders.jaxrs.dto.DuplicateInstanceExceptionDtoJaxb;
import es.udc.rs.orders.model.exceptions.DuplicateInstanceException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DuplicateInstanceExceptionMapper implements ExceptionMapper<DuplicateInstanceException> {

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(DuplicateInstanceException exception) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new DuplicateInstanceExceptionDtoJaxb(exception.getMessage()))
                .type(ExceptionMapperUtils.getResponseMediaType(headers))
                .build();
    }
}