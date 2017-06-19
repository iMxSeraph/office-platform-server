package io.muxin.office.server.entity.mysql;

import io.muxin.office.server.common.constant.SummaryStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * 签到概要表实体
 * Created by muxin on 2017/2/23.
 */
@Entity
@Table(name = "summary")
public class SummaryEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "date")
    private Date date;

    @Column(name = "work")
    private boolean work;

    @Column(name = "arrive_time")
    private Date arriveTime;

    @Column(name = "left_time")
    private Date leftTime;

    @Column(name = "late_time")
    private Integer lateTime;

    @Column(name = "after_time")
    private Integer afterTime;

    @Column(name = "early_time")
    private Integer earlyTime;

    @Column(name = "over_time")
    private Integer overTime;

    @Enumerated(EnumType.ORDINAL)
    private SummaryStatus status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public Date getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Date arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Date getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(Date leftTime) {
        this.leftTime = leftTime;
    }

    public Integer getLateTime() {
        return lateTime;
    }

    public void setLateTime(Integer lateTime) {
        this.lateTime = lateTime;
    }

    public Integer getAfterTime() {
        return afterTime;
    }

    public void setAfterTime(Integer afterTime) {
        this.afterTime = afterTime;
    }

    public Integer getEarlyTime() {
        return earlyTime;
    }

    public void setEarlyTime(Integer earlyTime) {
        this.earlyTime = earlyTime;
    }

    public Integer getOverTime() {
        return overTime;
    }

    public void setOverTime(Integer overTime) {
        this.overTime = overTime;
    }

    public SummaryStatus getStatus() {
        return status;
    }

    public void setStatus(SummaryStatus status) {
        this.status = status;
    }
}
