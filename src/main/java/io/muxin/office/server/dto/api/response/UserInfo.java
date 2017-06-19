package io.muxin.office.server.dto.api.response;

/**
 * 用户信息返回包
 * Created by muxin on 2017/2/26.
 */
public class UserInfo {

    /** 用户名 */
    private String username;

    /** 工号 */
    private String jid;

    /** 名称 */
    private String name;

    /** 惩罚时间 */
    private Integer punish;

    /** 实习生标志 */
    private Boolean internship;

    /** 管理员标志 */
    private Boolean admin;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPunish() {
        return punish;
    }

    public void setPunish(Integer punish) {
        this.punish = punish;
    }

    public Boolean getInternship() {
        return internship;
    }

    public void setInternship(Boolean internship) {
        this.internship = internship;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
