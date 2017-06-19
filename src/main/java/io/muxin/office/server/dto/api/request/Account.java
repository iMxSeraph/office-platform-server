package io.muxin.office.server.dto.api.request;

/**
 * 账号请求体
 * Created by muxin on 2017/2/24.
 */
public class Account {

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
