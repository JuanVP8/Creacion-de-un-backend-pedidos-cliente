package es.udc.rs.orders.jaxrs.config;

import org.glassfish.jersey.server.ResourceConfig;

public class OrdersAppConfig extends ResourceConfig {

    public OrdersAppConfig() {
        packages("es.udc.rs.orders.jaxrs");
    }
}