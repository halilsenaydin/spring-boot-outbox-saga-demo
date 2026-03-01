package io.github.halilsenaydin.inventory.order_service.core.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.halilsenaydin.shared.business.constants.OrderEventConstant;
import io.github.halilsenaydin.shared.core.config.SharedRabbitMQConfig;

@Configuration
public class RabbitMQConfig extends SharedRabbitMQConfig {
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(OrderEventConstant.ORDER_EXCHANGE, true, false);
    }

    @Bean
    public Queue createdQueue() {
        return new Queue(OrderEventConstant.ORDER_Q_CREATED, true);
    }

    @Bean
    public Queue creationFailedQueue() {
        return new Queue(OrderEventConstant.ORDER_Q_CREATION_FAILED, true);
    }

    @Bean
    public Queue confirmedQueue() {
        return new Queue(OrderEventConstant.ORDER_Q_CONFIRMED, true);
    }

    @Bean
    public Queue cancelledQueue() {
        return new Queue(OrderEventConstant.ORDER_Q_CANCELLED, true);
    }

    @Bean
    public Binding createdBinding() {
        return BindingBuilder
                .bind(createdQueue())
                .to(orderExchange())
                .with(OrderEventConstant.ORDER_RK_CREATED);
    }

    @Bean
    public Binding creationFailedBinding() {
        return BindingBuilder
                .bind(creationFailedQueue())
                .to(orderExchange())
                .with(OrderEventConstant.ORDER_RK_CREATION_FAILED);
    }

    @Bean
    public Binding confirmedBinding() {
        return BindingBuilder
                .bind(confirmedQueue())
                .to(orderExchange())
                .with(OrderEventConstant.ORDER_RK_CONFIRMED);
    }

    @Bean
    public Binding cancelledBinding() {
        return BindingBuilder
                .bind(cancelledQueue())
                .to(orderExchange())
                .with(OrderEventConstant.ORDER_RK_CANCELLED);
    }
}