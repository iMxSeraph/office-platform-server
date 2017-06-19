package io.muxin.office.server.web;

import io.muxin.office.server.common.constant.RollType;
import io.muxin.office.server.common.tool.DateTool;
import io.muxin.office.server.entity.mysql.RollEntity;
import io.muxin.office.server.entity.mysql.UserEntity;
import io.muxin.office.server.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

/**
 * WebSocket 控制器
 * Created by muxin on 2017/2/26.
 */
@Controller
public class WebSocketController {

    private SimpMessagingTemplate template;

    @Autowired
    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    private WebService webService;

    @Autowired
    public void setWebService(WebService webService) {
        this.webService = webService;
    }

    @MessageMapping("/roll/init")
    public void rollInit(UserEntity userEntity, Principal principal) {
        List<RollEntity> rollEntityList = webService.getRoll(DateTool.getToday(), RollType.TAKEOUT);
        template.convertAndSendToUser(userEntity.getUsername(), "/roll/takeout", rollEntityList);
        rollEntityList = webService.getRoll(DateTool.getToday(), RollType.SHARE);
        template.convertAndSendToUser(userEntity.getUsername(), "/roll/share", rollEntityList);
    }

    @MessageMapping("/roll/takeout")
    @SendTo("/roll/takeout")
    public RollEntity rollTakeout(UserEntity userEntity, Principal principal) {
        return webService.roll(userEntity, RollType.TAKEOUT);
    }

    @MessageMapping("/roll/share")
    @SendTo("/roll/share")
    public RollEntity rollShare(UserEntity userEntity, Principal principal) {
        return webService.roll(userEntity, RollType.SHARE);
    }
}
