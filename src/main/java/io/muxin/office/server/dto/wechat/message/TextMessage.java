package io.muxin.office.server.dto.wechat.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 文本类消息
 * Created by muxin on 2017/2/23.
 */
public class TextMessage {

    @JsonProperty("touser")
    private String toUser;

    @JsonProperty("msgtype")
    private String msgType;

    @JsonProperty("agentid")
    private Integer agentId;

    private Content text;

    private Integer safe;

    public TextMessage() {
        text = new Content();
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Content getText() {
        return text;
    }

    public void setText(Content text) {
        this.text = text;
    }

    public Integer getSafe() {
        return safe;
    }

    public void setSafe(Integer safe) {
        this.safe = safe;
    }
}
