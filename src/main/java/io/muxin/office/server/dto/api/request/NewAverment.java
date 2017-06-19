package io.muxin.office.server.dto.api.request;

import io.muxin.office.server.common.constant.AvermentType;

/**
 * 新建申辩请求体
 * Created by muxin on 2017/2/26.
 */
public class NewAverment {

    private int id;

    private AvermentType type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AvermentType getType() {
        return type;
    }

    public void setType(AvermentType type) {
        this.type = type;
    }
}
