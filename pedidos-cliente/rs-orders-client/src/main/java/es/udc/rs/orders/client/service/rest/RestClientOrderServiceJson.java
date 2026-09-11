package es.udc.rs.orders.client.service.rest;

import jakarta.ws.rs.core.MediaType;

public class RestClientOrderServiceJson extends RestClientOrderService {

    @Override
    protected MediaType getMediaType() {
        return MediaType.APPLICATION_JSON_TYPE;
    }
}