package io.muxin.office.server.common.tool;

import io.muxin.office.server.dto.wechat.message.TextMessage;

import java.util.Date;

/**
 * 消息工具类
 * Created by muxin on 2017/2/23.
 */
public class MessageTool {

    private static final String TEXT = "text";

    private static final int UNSAFE = 0;

    /**
     * 构造文本消息
     * @param username 接收用户名
     * @param text 内容
     * @return 消息体
     */
    public static TextMessage buildTextMessage(String username, String text) {
        TextMessage textMessage = new TextMessage();
        textMessage.setToUser(username);
        textMessage.setMsgType(TEXT);
        textMessage.setAgentId(5);
        textMessage.getText().setContent(text);
        textMessage.setSafe(UNSAFE);
        return textMessage;
    }

    /**
     * 构造签到成功消息
     * @param username 接受用户名
     * @param date 签到时间
     * @return 消息体
     */
    public static TextMessage buildSignInMessage(String username, Date date) {
        return buildTextMessage(username, "WiFi签到成功！\n" + DateTool.getYearMonthDayHourMinute(date));
    }

    /**
     * 发送消息
     * @param message 消息体
     */
    public static void sendMessage(TextMessage message) {

    }
}
