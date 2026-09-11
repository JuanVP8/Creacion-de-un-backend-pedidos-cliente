package es.udc.rs.orders.client.service.rest;

import jakarta.ws.rs.core.MediaType;

public class RestClientOrderServiceXml extends RestClientOrderService {

    @Override
    protected MediaType getMediaType() {
        return MediaType.APPLICATION_XML_TYPE;
    }
}