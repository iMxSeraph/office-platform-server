package io.muxin.office.server.repo.mysql;

import io.muxin.office.server.entity.mysql.UserEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * 用户表仓库
 * Created by muxin on 2017/2/24.
 */
public interface UserRepo extends CrudRepository<UserEntity, String> {

}
