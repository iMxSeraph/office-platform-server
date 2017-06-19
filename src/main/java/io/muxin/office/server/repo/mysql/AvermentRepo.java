package io.muxin.office.server.repo.mysql;

import io.muxin.office.server.common.constant.AvermentStatus;
import io.muxin.office.server.entity.mysql.AvermentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 申辩信息仓库
 * Created by muxin on 2017/2/26.
 */
public interface AvermentRepo extends CrudRepository<AvermentEntity, Integer> {

    List<AvermentEntity> findByStatus(AvermentStatus status);

}
