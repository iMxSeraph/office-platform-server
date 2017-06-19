package io.muxin.office.server.dto.api.request;

/**
 * 更新申辩请求体
 * Created by muxin on 2017/2/26.
 */
public class UpdateAverment {

    /** 是否批准 */
    private boolean approve;

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }
}
