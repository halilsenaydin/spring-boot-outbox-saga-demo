package io.github.halilsenaydin.shared.business.constants;

public class ProductEventConstant {
    // Exchange
    public static final String PRODUCT_EXCHANGE = "product-exchange";

    // Routing Keys
    public static final String PRODUCT_RK_CREATED = "product.created";
    public static final String PRODUCT_RK_CREATION_FAILED = "product.creation.failed";

    // Queue Names
    public static final String PRODUCT_Q_CREATED = "product-created-queue";
    public static final String PRODUCT_Q_CREATION_FAILED = "product-creation-failed-queue";
}
