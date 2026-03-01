package io.github.halilsenaydin.inventory.product_service.core.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.halilsenaydin.shared.business.constants.ProductEventConstant;
import io.github.halilsenaydin.shared.core.config.SharedRabbitMQConfig;

@Configuration
public class RabbitMQConfig extends SharedRabbitMQConfig {
    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(ProductEventConstant.PRODUCT_EXCHANGE, true, false);
    }

    @Bean
    public Queue createdQueue() {
        return new Queue(ProductEventConstant.PRODUCT_Q_CREATED, true);
    }

    @Bean
    public Queue creationFailedQueue() {
        return new Queue(ProductEventConstant.PRODUCT_Q_CREATION_FAILED, true);
    }

    @Bean
    public Binding createdBinding() {
        return BindingBuilder
                .bind(createdQueue())
                .to(productExchange())
                .with(ProductEventConstant.PRODUCT_RK_CREATED);
    }

    @Bean
    public Binding creationFailedBinding() {
        return BindingBuilder
                .bind(creationFailedQueue())
                .to(productExchange())
                .with(ProductEventConstant.PRODUCT_RK_CREATION_FAILED);
    }
}