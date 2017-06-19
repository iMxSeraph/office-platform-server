package io.muxin.office.server.config.listener;

import io.muxin.office.server.service.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 服务启动监听器
 * Created by muxin on 2017/2/24.
 */
@Component
public class ReadyListener implements ApplicationListener<ApplicationReadyEvent>{

    private static final Logger logger = LoggerFactory.getLogger(ReadyListener.class);

    private HttpService httpService;

    @Autowired
    public void setHttpService(HttpService httpService) {
        this.httpService = httpService;
    }

    @Value("${config.switch.access-token}")
    private boolean accessTokenSwitch;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if (null != httpService) {
            logger.info("监听到服务启动，开始初次更新 AccessToken");
            try {
                if (accessTokenSwitch) httpService.updateAccessToken();
            } catch (Exception e) {
                logger.error("更新 AccessToken 失败！");
            }
        } else {
            logger.info("监听到服务启动，但自动装配尚未就绪");
        }
    }
}
