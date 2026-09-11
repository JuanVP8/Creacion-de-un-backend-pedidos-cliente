package es.udc.rs.orders.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class ClientOrderServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "ClientOrderServiceFactory.className";
    private static ClientOrderService service = null;

    private ClientOrderServiceFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static ClientOrderService getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (ClientOrderService) serviceClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static ClientOrderService getService() {

        if (service == null) {
            service = getInstance();
        }

        return service;
    }
}
