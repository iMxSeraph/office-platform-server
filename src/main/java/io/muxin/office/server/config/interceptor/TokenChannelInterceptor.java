package io.muxin.office.server.config.interceptor;

import io.muxin.office.server.common.constant.ConstValue;
import io.muxin.office.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * WebSocket Token拦截器
 * Created by muxin on 2017/2/27.
 */
@Component
public class TokenChannelInterceptor extends ChannelInterceptorAdapter {

    private TokenService tokenService;

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> tokenList = accessor.getNativeHeader(ConstValue.TOKEN_HEADER);
            if (null != tokenList && tokenList.size() > 0 && tokenService.checkToken(tokenList.get(0))) {
                accessor.setUser(tokenService.getToken(tokenList.get(0))::getUsername);
            }
        }
        return message;
    }
}
