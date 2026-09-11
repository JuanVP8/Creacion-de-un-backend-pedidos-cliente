package es.udc.rs.orders.model.orderservice;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class OrderServiceFactory {

	private static final String CLASS_NAME_PARAMETER = "OrderServiceFactory.className";
	private static OrderService service = null;

	private OrderServiceFactory() {
	}

	@SuppressWarnings("rawtypes")
	private static OrderService getInstance() {
		try {
			String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
			Class serviceClass = Class.forName(serviceClassName);
			return (OrderService) serviceClass.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static synchronized  OrderService getService() {

		if (service == null) {
			service = getInstance();
		}
		return service;

	}
}
