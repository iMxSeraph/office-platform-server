package io.muxin.office.server.entity.mysql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户设备表
 * Created by muxin on 2017/2/23.
 */
@Entity
@Table(name = "device")
public class DeviceEntity {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "mac_address")
    private String macAddress;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

}
