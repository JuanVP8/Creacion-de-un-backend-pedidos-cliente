package es.udc.rs.orders.client.ui;

import java.util.List;

import es.udc.rs.orders.client.service.ClientOrderService;
import es.udc.rs.orders.client.service.ClientOrderServiceFactory;
import es.udc.rs.orders.client.service.rest.dto.ClientCustomerDtoJaxb;
import es.udc.rs.orders.client.service.rest.dto.ClientOrderDtoJaxb;
import es.udc.rs.orders.client.service.rest.dto.ClientOrderLineDtoJaxb;
import es.udc.rs.orders.client.service.rest.dto.ClientOrderSummaryDtoJaxb;

public class OrderServiceClient {

    public static void main(String[] args) {

        if (args.length == 0) {
            printUsageAndExit();
        }

        ClientOrderService clientOrderService = ClientOrderServiceFactory.getService();

        if ("-addCustomer".equalsIgnoreCase(args[0])) {

            validateArgs(args, 5, new int[] {});

            try {
                ClientCustomerDtoJaxb customer = clientOrderService.addCustomer(
                        args[1],
                        args[2],
                        args[3],
                        args[4]);

                System.out.println("Customer " + customer.getCustomerId() + " created successfully");
                System.out.println("Name: " + customer.getName());
                System.out.println("DNI: " + customer.getDni());
                System.out.println("Address: " + customer.getAddress());
                System.out.println("Email: " + customer.getEmail());

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if ("-removeCustomer".equalsIgnoreCase(args[0])) {

            validateArgs(args, 2, new int[] { 1 });

            try {
                Long customerId = Long.valueOf(args[1]);

                clientOrderService.removeCustomer(customerId);

                System.out.println("Customer " + customerId + " removed successfully");

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if ("-findOrdersByCustomer".equalsIgnoreCase(args[0])) {

            if (args.length != 4 && args.length != 5) {
                printUsageAndExit();
            }

            validateNumericArgument(args[1]);
            validateNumericArgument(args[2]);
            validateNumericArgument(args[3]);

            try {
                Long customerId = Long.valueOf(args[1]);
                Integer from = Integer.valueOf(args[2]);
                Integer max = Integer.valueOf(args[3]);
                String status = args.length == 5 ? args[4] : null;

                List<ClientOrderSummaryDtoJaxb> orders =
                        clientOrderService.findOrdersByCustomer(customerId, status, from, max);

                if (orders.isEmpty()) {
                    System.out.println("No orders found");
                } else {
                    System.out.println("Orders found:");

                    for (ClientOrderSummaryDtoJaxb order : orders) {
                        printOrderSummary(order);
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if ("-changeOrderStatus".equalsIgnoreCase(args[0])) {

            validateArgs(args, 3, new int[] { 1 });

            try {
                Long orderId = Long.valueOf(args[1]);
                String status = args[2];

                ClientOrderDtoJaxb order =
                        clientOrderService.changeOrderStatus(orderId, status);

                System.out.println("Order status changed successfully");
                printOrder(order);

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else {
            printUsageAndExit();
        }
    }

    private static void printOrderSummary(ClientOrderSummaryDtoJaxb order) {
        System.out.println("----------------------------------------");
        System.out.println("Order id: " + order.getOrderId());
        System.out.println("Date: " + order.getOrderDate());
        System.out.println("Total price: " + order.getTotalPrice());
        System.out.println("----------------------------------------");
    }

    private static void printOrder(ClientOrderDtoJaxb order) {
        System.out.println("----------------------------------------");
        System.out.println("Order id: " + order.getOrderId());
        System.out.println("Customer id: " + order.getCustomerId());
        System.out.println("Date: " + order.getOrderDate());
        System.out.println("Status: " + order.getOrderStatus());

        if (order.getOrderLines() != null && !order.getOrderLines().isEmpty()) {
            System.out.println("Lines:");

            for (ClientOrderLineDtoJaxb line : order.getOrderLines()) {
                System.out.println("    Product id: " + line.getProductId()
                        + ", quantity: " + line.getQuantity()
                        + ", price: " + line.getPrice());
            }
        }

        System.out.println("----------------------------------------");
    }

    public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {

        if (expectedArgs != args.length) {
            printUsageAndExit();
        }

        for (int position : numericArguments) {
            validateNumericArgument(args[position]);
        }
    }

    private static void validateNumericArgument(String argument) {
        try {
            Long.valueOf(argument);
        } catch (NumberFormatException e) {
            printUsageAndExit();
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("""
                Usage:

                    [-addCustomer]
                        OrderServiceClient -addCustomer <name> <dni> <address> <email>

                    [-removeCustomer]
                        OrderServiceClient -removeCustomer <customerId>

                    [-findOrdersByCustomer]
                        OrderServiceClient -findOrdersByCustomer <customerId> <from> <max> [status]

                        status opcional:
                            PENDING
                            PROCESSING
                            PROCESSED

                    [-changeOrderStatus]
                        OrderServiceClient -changeOrderStatus <orderId> <PROCESSING|PROCESSED>
                """);
    }
}