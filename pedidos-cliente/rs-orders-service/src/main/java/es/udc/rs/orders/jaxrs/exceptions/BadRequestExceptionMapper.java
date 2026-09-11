package es.udc.rs.orders.jaxrs.exceptions;

import es.udc.rs.orders.jaxrs.dto.InputValidationExceptionDtoJaxb;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(BadRequestException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new InputValidationExceptionDtoJaxb(exception.getMessage()))
                .type(ExceptionMapperUtils.getResponseMediaType(headers))
                .build();
    }
}