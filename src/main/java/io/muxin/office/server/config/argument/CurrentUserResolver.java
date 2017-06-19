package io.muxin.office.server.config.argument;

import io.muxin.office.server.common.constant.ConstValue;
import io.muxin.office.server.config.annotation.Authentication;
import io.muxin.office.server.config.annotation.CurrentUser;
import io.muxin.office.server.entity.redis.TokenEntity;
import io.muxin.office.server.entity.mysql.UserEntity;
import io.muxin.office.server.repo.mysql.UserRepo;
import io.muxin.office.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 当前用户注入类
 * Created by muxin on 2017/2/25.
 */
@Component
public class CurrentUserResolver implements HandlerMethodArgumentResolver {

    private TokenService tokenService;

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    private UserRepo userRepo;

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        // 如果参数类型是 UserEntity 并且有 @CurrentUser 注解则注入
        return null != methodParameter.getMethod().getAnnotation(Authentication.class)
                && methodParameter.getParameterType().isAssignableFrom(UserEntity.class)
                && methodParameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest httpServletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String token = WebUtils.getCookie(httpServletRequest, ConstValue.TOKEN_HEADER).getValue();
        TokenEntity tokenEntity = tokenService.getToken(token);
        return userRepo.findOne(tokenEntity.getUsername());
    }
}
