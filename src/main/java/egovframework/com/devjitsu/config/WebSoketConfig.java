package egovframework.com.devjitsu.config;

import egovframework.com.cmm.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSoketConfig implements WebSocketConfigurer {

    private final HandshakeInterceptor handshakeInterceptor;

    private final WebSocketHandler testWebsocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(testWebsocketHandler, "/ws")
                .addInterceptors(handshakeInterceptor)
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
