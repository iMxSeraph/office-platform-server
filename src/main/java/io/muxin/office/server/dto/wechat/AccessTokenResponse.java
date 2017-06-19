package io.muxin.office.server.dto.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 请求 AccessToken 返回体
 * Created by muxin on 2017/2/24.
 */
public class AccessTokenResponse {

    @JsonProperty("access_token")
    private String accssToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    public String getAccssToken() {
        return accssToken;
    }

    public void setAccssToken(String accssToken) {
        this.accssToken = accssToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
