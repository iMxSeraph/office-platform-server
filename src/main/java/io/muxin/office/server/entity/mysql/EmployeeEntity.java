package io.muxin.office.server.entity.mysql;

import io.muxin.office.server.common.constant.EmployeeSeries;

import javax.persistence.*;

/**
 * 员工实体
 * Created by muxin on 2017/2/23.
 */
@Entity
@Table(name = "employee")
public class EmployeeEntity {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "jid")
    private String jid;

    @Column(name = "id")
    private Integer id;

    @Column(name = "punish")
    private int punish;

    @Column(name = "internship")
    private boolean internship;

    @Column(name = "admin")
    private boolean admin;

    @Column(name = "gamer")
    private boolean gamer;

    @Column(name = "series")
    @Enumerated(EnumType.ORDINAL)
    private EmployeeSeries series;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getPunish() {
        return punish;
    }

    public void setPunish(int punish) {
        this.punish = punish;
    }

    public boolean isInternship() {
        return internship;
    }

    public void setInternship(boolean internship) {
        this.internship = internship;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isGamer() {
        return gamer;
    }

    public void setGamer(boolean gamer) {
        this.gamer = gamer;
    }

    public EmployeeSeries getSeries() {
        return series;
    }

    public void setSeries(EmployeeSeries series) {
        this.series = series;
    }
}
