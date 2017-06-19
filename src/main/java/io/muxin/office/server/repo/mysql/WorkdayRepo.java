package io.muxin.office.server.repo.mysql;

import io.muxin.office.server.entity.mysql.WorkdayEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

/**
 * 工作日实体仓库
 * Created by muxin on 2017/3/1.
 */
public interface WorkdayRepo extends CrudRepository<WorkdayEntity, Date> {
}
