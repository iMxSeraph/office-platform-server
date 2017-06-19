package io.muxin.office.server.entity.mysql;

import javax.persistence.*;

/**
 * 用户表实体
 * Created by muxin on 2017/2/24.
 */
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    private String username;

    private String password;

    @Column(name = "forgot_token")
    private String forgotToken;

    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private EmployeeEntity employee;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getForgotToken() {
        return forgotToken;
    }

    public void setForgotToken(String forgotToken) {
        this.forgotToken = forgotToken;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }
}
