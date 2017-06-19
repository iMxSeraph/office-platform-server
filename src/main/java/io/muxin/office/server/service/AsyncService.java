package io.muxin.office.server.service;

import io.muxin.office.server.common.tool.MessageTool;
import io.muxin.office.server.dto.wechat.message.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 异步服务
 * Created by muxin on 2017/2/24.
 */
@Service
public class AsyncService {

    private HttpService httpService;

    @Autowired
    public void setHttpService(HttpService httpService) {
        this.httpService = httpService;
    }

    /**
     * 异步通知用户签到成功
     * @param username 用户名
     * @param time 签到时间
     */
    @Async
    public void notifySignIn(String username, Date time) throws Exception {
        TextMessage message = MessageTool.buildSignInMessage(username, time);
        httpService.sendMessage(message);
    }

}
