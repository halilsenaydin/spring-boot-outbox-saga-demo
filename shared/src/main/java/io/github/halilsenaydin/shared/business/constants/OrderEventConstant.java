package io.github.halilsenaydin.shared.business.constants;

public class OrderEventConstant {
    // Exchange
    public static final String ORDER_EXCHANGE = "order-exchange";

    // Routing Keys
    public static final String ORDER_RK_CREATED = "order.created";
    public static final String ORDER_RK_CREATION_FAILED = "order.creation.failed";
    public static final String ORDER_RK_CONFIRMED = "order.confirmed";
    public static final String ORDER_RK_CANCELLED = "order.cancelled";

    // Queue Names
    public static final String ORDER_Q_CREATED = "order-created-queue";
    public static final String ORDER_Q_CREATION_FAILED = "order-creation-failed-queue";
    public static final String ORDER_Q_CONFIRMED = "order-confirmed-queue";
    public static final String ORDER_Q_CANCELLED = "order-cancelled-queue";
}
