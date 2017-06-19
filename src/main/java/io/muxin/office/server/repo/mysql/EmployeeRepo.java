package io.muxin.office.server.repo.mysql;

import io.muxin.office.server.entity.mysql.EmployeeEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * 员工表Repo
 * Created by muxin on 2017/2/23.
 */
public interface EmployeeRepo extends CrudRepository<EmployeeEntity, String> {
}
