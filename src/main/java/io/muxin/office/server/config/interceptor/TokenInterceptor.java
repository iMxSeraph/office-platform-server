package io.muxin.office.server.config.interceptor;

import io.muxin.office.server.common.constant.AuthType;
import io.muxin.office.server.config.annotation.Authentication;
import io.muxin.office.server.common.constant.ConstValue;
import io.muxin.office.server.entity.mysql.UserEntity;
import io.muxin.office.server.entity.redis.TokenEntity;
import io.muxin.office.server.repo.mysql.UserRepo;
import io.muxin.office.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token 拦截器
 * Created by muxin on 2017/2/25.
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

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
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (o instanceof HandlerMethod && ((HandlerMethod) o).getMethod().getAnnotation(Authentication.class) != null) {
            // 如果是映射到方法并且有鉴权注解
            Cookie cookie = WebUtils.getCookie(httpServletRequest, ConstValue.TOKEN_HEADER);
            if (null != cookie && tokenService.checkToken(cookie.getValue())) {
                TokenEntity tokenEntity = tokenService.getToken(cookie.getValue());
                UserEntity userEntity = userRepo.findOne(tokenEntity.getUsername());
                if (((HandlerMethod) o).getMethod().getAnnotation(Authentication.class).value() == AuthType.ADMIN) {
                    if (userEntity.getEmployee().isAdmin()) {
                        return true;
                    } else {
                        httpServletResponse.setStatus(403);
                        return false;
                    }
                } else {
                    return true;
                }
            } else {
                httpServletResponse.setStatus(401);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
