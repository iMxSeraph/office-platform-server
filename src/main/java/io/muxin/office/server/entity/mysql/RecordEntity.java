package io.muxin.office.server.entity.mysql;

import javax.persistence.*;
import java.util.Date;

/**
 * 签到记录表实体
 * Created by muxin on 2017/2/23.
 */
@Entity
@Table(name = "record")
public class RecordEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "record")
    private Date record;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getRecord() {
        return record;
    }

    public void setRecord(Date record) {
        this.record = record;
    }

}
