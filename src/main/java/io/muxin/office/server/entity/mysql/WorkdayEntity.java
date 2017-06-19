package io.muxin.office.server.entity.mysql;

import javax.persistence.*;
import java.util.Date;

/**
 * 工作日信息
 * Created by muxin on 2017/3/1.
 */
@Entity
@Table(name = "workday")
public class WorkdayEntity {

    @Id
    private Date date;

    private boolean work;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isWork() {
        return work;
    }

    public void setWork(boolean work) {
        this.work = work;
    }
}
