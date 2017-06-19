package io.muxin.office.server.repo.mssql;

import io.muxin.office.server.entity.mssql.CheckInOutEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * 签到机数据 Repo
 * Created by muxin on 2017/2/23.
 */
public interface CheckInOutRepo extends CrudRepository<CheckInOutEntity, Integer> {
    @Query("SELECT o FROM CheckInOutEntity o WHERE o.key.userId = ?1 AND o.key.checkTime >= ?2 AND o.key.checkTime <= ?3")
    List<CheckInOutEntity> getRecord(Integer userId, Date start, Date end);
}
