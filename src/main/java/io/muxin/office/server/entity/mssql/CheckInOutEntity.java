package io.muxin.office.server.entity.mssql;

import javax.persistence.*;

/**
 * 签到机签到数据
 * Created by muxin on 2017/2/23.
 */
@Entity
@Table(name = "CHECKINOUT")
public class CheckInOutEntity {
    @EmbeddedId
    private Key key;

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }
}
