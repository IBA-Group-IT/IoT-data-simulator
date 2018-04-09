package com.iba.iot.datasimulator.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
@EnableRabbit
public class RabbitMqConfig implements RabbitListenerConfigurer {

    @Bean
    Queue sessionStatesQueue() {
        return new Queue("sessionStates", false);
    }

    @Bean
    Queue sessionErrorsQueue() {
        return new Queue("sessionErrors", false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("amq.topic");
    }

    @Bean
    Binding sessionBinding(Queue sessionStatesQueue, TopicExchange exchange) {
        return BindingBuilder.bind(sessionStatesQueue).to(exchange).with("sessions.*");
    }

    @Bean
    Binding errorsBinding(Queue sessionErrorsQueue, TopicExchange exchange) {
        return BindingBuilder.bind(sessionErrorsQueue).to(exchange).with("sessions.*.errors");
    }

    @Bean
    public MappingJackson2MessageConverter jackson2Converter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        return converter;
    }

    @Bean
    public DefaultMessageHandlerMethodFactory customHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(jackson2Converter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(customHandlerMethodFactory());
    }

}
