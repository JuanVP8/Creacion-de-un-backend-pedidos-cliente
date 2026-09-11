package es.udc.rs.orders.jaxrs.exceptions;

import es.udc.rs.orders.jaxrs.dto.InputValidationExceptionDtoJaxb;
import es.udc.rs.orders.model.exceptions.InputValidationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InputValidationExceptionMapper implements ExceptionMapper<InputValidationException> {

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(InputValidationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new InputValidationExceptionDtoJaxb(exception.getMessage()))
                .type(ExceptionMapperUtils.getResponseMediaType(headers))
                .build();
    }
}