package es.udc.rs.orders.client.service.rest;

import java.util.List;

import es.udc.rs.orders.client.service.ClientOrderService;
import es.udc.rs.orders.client.service.rest.dto.ClientCustomerDtoJaxb;
import es.udc.rs.orders.client.service.rest.dto.ClientOrderDtoJaxb;
import es.udc.rs.orders.client.service.rest.dto.ClientOrderSummaryDtoJaxb;
import es.udc.rs.orders.client.service.rest.dto.ClientOrderSummaryDtoJaxbList;
import es.udc.rs.orders.client.service.rest.dto.ClientStatusDtoJaxb;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public abstract class RestClientOrderService implements ClientOrderService {

    private static Client client = null;

    private static final String ENDPOINT_ADDRESS_PARAMETER =
            "RestClientOrderService.endpointAddress";

    private WebTarget endPointWebTarget = null;

    private static Client getClient() {
        if (client == null) {
            client = ClientBuilder.newClient();
        }

        return client;
    }

    private WebTarget getEndpointWebTarget() {
        if (endPointWebTarget == null) {
            endPointWebTarget = getClient()
                    .target(ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER));
        }

        return endPointWebTarget;
    }

    protected abstract MediaType getMediaType();

    @Override
    public ClientCustomerDtoJaxb addCustomer(
            String name, String dni, String address, String email) {

        ClientCustomerDtoJaxb customerDto =
                new ClientCustomerDtoJaxb(null, name, dni, address, email);

        return getEndpointWebTarget()
                .path("customers")
                .request(getMediaType())
                .post(Entity.entity(customerDto, getMediaType()), ClientCustomerDtoJaxb.class);
    }

    @Override
	public void removeCustomer(Long customerId) {

    	Response response = getEndpointWebTarget()
            .path("customers")
            .path(customerId.toString())
            .request(getMediaType())
            .delete();

    	if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
        String errorMessage;

        try {
            errorMessage = response.readEntity(String.class);
        } catch (Exception e) {
            errorMessage = "No se pudo leer el cuerpo de la respuesta de error";
        }

        throw new RuntimeException(
                "Error al eliminar el cliente. HTTP status: "
                        + response.getStatus()
                        + ". Respuesta: "
                        + errorMessage);
    }
}

    @Override
    public List<ClientOrderSummaryDtoJaxb> findOrdersByCustomer(
            Long customerId, String status, Integer from, Integer max) {

        WebTarget target = getEndpointWebTarget()
                .path("orders")
                .path("customer")
                .path(customerId.toString())
                .queryParam("from", from)
                .queryParam("max", max);

        if (status != null && !status.isBlank()) {
            target = target.queryParam("status", status);
        }

        ClientOrderSummaryDtoJaxbList result = target
                .request(getMediaType())
                .get(ClientOrderSummaryDtoJaxbList.class);

        return result.getOrders();
    }

    @Override
    public ClientOrderDtoJaxb changeOrderStatus(Long orderId, String status) {

        ClientStatusDtoJaxb statusDto = new ClientStatusDtoJaxb(status);

        return getEndpointWebTarget()
                .path("orders")
                .path(orderId.toString())
                .path("status")
                .request(getMediaType())
                .put(Entity.entity(statusDto, getMediaType()), ClientOrderDtoJaxb.class);
    }
}