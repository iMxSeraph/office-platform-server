package io.muxin.office.server.config.argument;

import io.muxin.office.server.entity.mysql.UserEntity;
import io.muxin.office.server.repo.mysql.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;

/**
 * WebSocket 用户注入类
 * Created by muxin on 2017/2/26.
 */
@Component
public class WebSocketUserResolver implements HandlerMethodArgumentResolver {

    private UserRepo userRepo;

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(UserEntity.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, Message<?> message) throws Exception {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.getAccessor(message, SimpMessageHeaderAccessor.class);
        return null != accessor.getUser() ? userRepo.findOne(accessor.getUser().getName()) : null;
    }
}
