package io.muxin.office.server.repo.mysql;

import io.muxin.office.server.common.constant.RollType;
import io.muxin.office.server.entity.mysql.EmployeeEntity;
import io.muxin.office.server.entity.mysql.RollEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Roll点信息仓库
 * Created by muxin on 2017/2/27.
 */
public interface RollRepo extends CrudRepository<RollEntity, String> {
    RollEntity findByEmployeeAndDateAndType(EmployeeEntity employee, Date date, RollType type);
    RollEntity findByDateAndTypeAndRoll(Date date, RollType type, int roll);
    List<RollEntity> findByDateAndType(Date date, RollType type);
}
