package io.muxin.office.server.task;

import io.muxin.office.server.service.SignService;
import io.muxin.office.server.service.HttpService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;

/**
 * 定时任务类
 * Created by muxin on 2017/2/23.
 */
@Component
public class TaskSchedule {

    private static final Logger logger = LoggerFactory.getLogger(TaskSchedule.class);

    private SignService signService;

    @Autowired
    public void setSignService(SignService signService) {
        this.signService = signService;
    }

    private HttpService httpService;

    @Autowired
    public void setHttpService(HttpService httpService) {
        this.httpService = httpService;
    }

    @Value("${config.switch.sign}")
    private boolean signSwitch;
    @Value("${config.switch.access-token}")
    private boolean accessTokenSwitch;
    @Value("${config.switch.calculate}")
    private boolean calculate;
    /** 扫描MAC地址的命令 */
    private static final String SCAN_COMMAND = "nmap -sP 192.168.1.0/24";

    /**
     * 每日签到签到
     */
    @Scheduled(cron = "0 * 6-23 * * *")
    public void doSignIn() throws Exception {
        if (!signSwitch) return;
        // 生成当前时刻
        logger.info("开始执行签到扫描");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        Process exeEcho = Runtime.getRuntime().exec(SCAN_COMMAND);
        signService.doSignIn(IOUtils.toString(exeEcho.getInputStream(), StandardCharsets.UTF_8), calendar.getTime());
    }

    /**
     * 更新AccessToken
     */
    @Scheduled(cron = "0 0 */2 * * *")
    public void updateAccessToken() throws Exception {
        if (!accessTokenSwitch) return;
        logger.info("自动更新 AccessToken");
        httpService.updateAccessToken();
    }

    /**
     * 每日12点计算昨日考勤
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void doSummary() {
        if (!calculate) return;
        logger.info("自动计算前一日签到");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        signService.doSummary(calendar.getTime(), calendar.getTime());
    }
}
