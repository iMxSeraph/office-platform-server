package io.muxin.office.server.service;

import org.springframework.stereotype.Service;

/**
 * 数据服务类，用于保存数据
 * Created by muxin on 2017/2/24.
 */
@Service
public class DataService {

    /** AccessToken */
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
