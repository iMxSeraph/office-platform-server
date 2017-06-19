package io.muxin.office.server.dto.api.response;

import io.muxin.office.server.entity.mysql.SummaryEntity;

import java.util.List;

/**
 * 全部数据返回包
 * Created by muxin on 2017/2/26.
 */
public class AllSummary {

    private UserInfo userInfo;

    private List<SummaryEntity> summaries;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<SummaryEntity> getSummaries() {
        return summaries;
    }

    public void setSummaries(List<SummaryEntity> summaries) {
        this.summaries = summaries;
    }

}
