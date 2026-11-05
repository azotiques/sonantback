package org.azotiques.sonantv1.realtime.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtChannelInterceptor jwtChannelInterceptor;

    @Value("${ALLOWED_ORIGINS}")
    private String[] ALLOWED_ORIGINS;

    @Value("${RABBITMQ_HOST:localhost}")
    private String RABBITMQ_HOST;

    @Value("${RABBITMQ_STOMP_PORT:61613}")
    private int RABBITMQ_STOMP_PORT;

    @Value("${RABBITMQ_LOGIN}")
    private String RABBITMQ_LOGIN;

    @Value("${RABBITMQ_PASS}")
    private String RABBITMQ_PASS;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins(ALLOWED_ORIGINS);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtChannelInterceptor);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");

        registry.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost(RABBITMQ_HOST)
                .setRelayPort(RABBITMQ_STOMP_PORT)
                .setClientLogin(RABBITMQ_LOGIN)
                .setClientPasscode(RABBITMQ_PASS)
                .setSystemLogin(RABBITMQ_LOGIN)
                .setSystemPasscode(RABBITMQ_PASS);
    }

}
