package es.udc.rs.orders.jaxrs.exceptions;

import java.util.List;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

public final class ExceptionMapperUtils {

    private ExceptionMapperUtils() {
    }

    public static MediaType getResponseMediaType(HttpHeaders headers) {
        List<MediaType> acceptableMediaTypes = headers.getAcceptableMediaTypes();

        for (MediaType mediaType : acceptableMediaTypes) {
            if (mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
                return MediaType.APPLICATION_JSON_TYPE;
            }
        }

        return MediaType.APPLICATION_XML_TYPE;
    }
}