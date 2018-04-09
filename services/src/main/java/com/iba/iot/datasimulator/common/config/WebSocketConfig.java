package com.iba.iot.datasimulator.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iba.iot.datasimulator.common.spring.CustomHandshakeHandler;
import com.iba.iot.datasimulator.common.spring.CustomUserDestinationResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.simp.user.UserDestinationResolver;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.HandshakeHandler;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Value("${rabbit.mq.host}")
    private String rabbitMqHost;

    @Value("${rabbit.mq.port}")
    private int rabbitMqPort;

    @Value("${rabbit.mq.user}")
    private String rabbitMqUser;

    @Value("${rabbit.mq.password}")
    private String rabbitMqPassword;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        config.setApplicationDestinationPrefixes("/");

        config.enableStompBrokerRelay("/topic", "/queue")
              .setRelayHost(rabbitMqHost)
              .setRelayPort(rabbitMqPort)
              .setSystemLogin(rabbitMqUser)
              .setSystemPasscode(rabbitMqPassword)
              .setClientLogin(rabbitMqUser)
              .setClientPasscode(rabbitMqPassword);

        config.setPathMatcher(new AntPathMatcher("."));
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setHandshakeHandler(handshakeHandler())
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Bean
    public UserDestinationResolver userDestinationResolver(SimpUserRegistry userRegistry) {
        return new CustomUserDestinationResolver(userRegistry);
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {

        boolean result = super.configureMessageConverters(messageConverters);
        messageConverters.add(0, jacksonConverter());

        return result;
    }

    @Bean
    MappingJackson2MessageConverter jacksonConverter() {

        ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Bean
    public HandshakeHandler handshakeHandler() {
        return new CustomHandshakeHandler();
    }
}
