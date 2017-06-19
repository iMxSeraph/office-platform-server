package io.muxin.office.server.entity.mysql;

import io.muxin.office.server.common.constant.RollType;

import javax.persistence.*;
import java.util.Date;

/**
 * Roll点实体
 * Created by muxin on 2017/2/27.
 */
@Entity
@Table(name = "roll")
public class RollEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private Date date;

    @Enumerated(EnumType.ORDINAL)
    private RollType type;

    private int roll;

    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private EmployeeEntity employee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RollType getType() {
        return type;
    }

    public void setType(RollType type) {
        this.type = type;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }
}
