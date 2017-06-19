package io.muxin.office.server.repo.mysql;

import io.muxin.office.server.entity.mysql.DeviceEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * 设备表仓库
 * Created by muxin on 2017/2/23.
 */
public interface DeviceRepo extends CrudRepository<DeviceEntity, String> {
}
