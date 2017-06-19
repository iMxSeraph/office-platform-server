package io.muxin.office.server.config;

import io.muxin.office.server.config.argument.WebSocketUserResolver;
import io.muxin.office.server.config.interceptor.TokenChannelInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.util.List;

/**
 * WebSocket配置类
 * Created by muxin on 2017/1/11.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    private TokenChannelInterceptor tokenChannelInterceptor;

    @Autowired
    public void setTokenChannelInterceptor(TokenChannelInterceptor tokenChannelInterceptor) {
        this.tokenChannelInterceptor = tokenChannelInterceptor;
    }

    private WebSocketUserResolver webSocketUserResolver;

    @Autowired
    public void setWebSocketUserResolver(WebSocketUserResolver webSocketUserResolver) {
        this.webSocketUserResolver = webSocketUserResolver;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/roll");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/webSocket").setAllowedOrigins("*");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(tokenChannelInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(webSocketUserResolver);
    }
}
