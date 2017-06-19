package io.muxin.office.server.service;

import io.muxin.office.server.entity.redis.TokenEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * TokenEntity 服务类
 * Created by muxin on 2017/2/24.
 */
@Service
public class TokenService {

    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 创建 Token
     * @param username 用户名
     * @return Token
     */
    public TokenEntity createToken(String username) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(UUID.randomUUID().toString());
        tokenEntity.setUsername(username);
        redisTemplate.boundValueOps(tokenEntity.getToken()).set(tokenEntity.getUsername(), 30, TimeUnit.DAYS);
        return tokenEntity;
    }

    /**
     * 获取 Token
     * @param token Token
     * @return Token实体
     */
    public TokenEntity getToken(String token) {
        String username = redisTemplate.opsForValue().get(token);
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setUsername(username);
        return tokenEntity;
    }

    /**
     * 检查 Token 是否有效
     * @param token Token
     * @return 是否有效
     */
    public boolean checkToken(String token) {
        return null != token && null != redisTemplate.opsForValue().get(token);
    }

    /**
     * 销毁 Token
     * @param token Token
     */
    public void destroyToken(String token) {
        redisTemplate.delete(token);
    }
}
