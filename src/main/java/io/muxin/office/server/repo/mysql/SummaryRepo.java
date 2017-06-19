package io.muxin.office.server.repo.mysql;

import io.muxin.office.server.entity.mysql.SummaryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * 签到概要表仓库
 * Created by muxin on 2017/2/24.
 */
public interface SummaryRepo extends CrudRepository<SummaryEntity, Integer> {

    SummaryEntity findByUsernameAndDate(String username, Date date);
    List<SummaryEntity> findByUsernameAndDateBetween(String username, Date start, Date end);

}
