package io.muxin.office.server.dto.api.response;

import io.muxin.office.server.entity.mysql.AvermentEntity;
import io.muxin.office.server.entity.mysql.UserEntity;

/**
 * 全部签到信息返回包
 * Created by muxin on 2017/3/3.
 */
public class AllAverment {

    private UserInfo userInfo;

    private AvermentEntity averment;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public AvermentEntity getAverment() {
        return averment;
    }

    public void setAverment(AvermentEntity averment) {
        this.averment = averment;
    }
}
