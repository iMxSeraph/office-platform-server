package io.muxin.office.server.repo.mysql;

import io.muxin.office.server.entity.mysql.RecordEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * 签到记录表仓库
 * Created by muxin on 2017/2/23.
 */
public interface RecordRepo extends CrudRepository<RecordEntity, Long> {

    List<RecordEntity> findByUsernameAndRecordBetween(String username, Date start, Date end);

}
