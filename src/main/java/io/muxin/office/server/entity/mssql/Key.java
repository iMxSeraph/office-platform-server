package io.muxin.office.server.entity.mssql;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

/**
 * 特殊主键
 * Created by muxin on 2017/2/24.
 */
@Embeddable
public class Key implements Serializable {

    @Column(name = "USERID")
    private Integer userId;

    @Column(name = "CHECKTIME")
    private Date checkTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }
}
