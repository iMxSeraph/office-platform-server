package io.muxin.office.server.entity.redis;

/**
 * TokenEntity
 * Created by muxin on 2017/2/24.
 */
public class TokenEntity {

    /** Token */
    private String token;

    /** 用户名 */
    private String username;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
