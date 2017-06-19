package io.muxin.office.server.service;

import io.muxin.office.server.common.constant.*;
import io.muxin.office.server.common.tool.DateTool;
import io.muxin.office.server.dto.api.response.AllAverment;
import io.muxin.office.server.dto.api.response.AllSummary;
import io.muxin.office.server.dto.api.response.UserInfo;
import io.muxin.office.server.entity.mysql.*;
import io.muxin.office.server.repo.mysql.*;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 网页相关服务类
 * Created by muxin on 2017/2/24.
 */
@Service
public class WebService {

    private static final int MAX_ROLL = 101;

    private UserRepo userRepo;

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    private SummaryRepo summaryRepo;

    @Autowired
    public void setSummaryRepo(SummaryRepo summaryRepo) {
        this.summaryRepo = summaryRepo;
    }

    private EmployeeRepo employeeRepo;

    @Autowired
    public void setEmployeeRepo(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    private AvermentRepo avermentRepo;

    @Autowired
    public void setAvermentRepo(AvermentRepo avermentRepo) {
        this.avermentRepo = avermentRepo;
    }

    private RollRepo rollRepo;

    @Autowired
    public void setRollRepo(RollRepo rollRepo) {
        this.rollRepo = rollRepo;
    }

    /**
     * 登陆
     * @param username 用户名
     * @param password 密码
     * @return 用户实体
     */
    public UserEntity login(String username, String password) {
        UserEntity userEntity = userRepo.findOne(username);
        if (null != userEntity && BCrypt.checkpw(password, userEntity.getPassword())) {
            return userEntity;
        } else {
            return null;
        }
    }

    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @return 用户实体
     */
    public UserEntity register(String username, String password) {
        EmployeeEntity employeeEntity = employeeRepo.findOne(username);
        if (null == employeeEntity) return null;
        UserEntity userEntity = userRepo.findOne(username);
        if (null != userEntity) return null;
        userEntity = new UserEntity();
        userEntity.setEmployee(employeeEntity);
        userEntity.setUsername(username);
        userEntity.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        userRepo.save(userEntity);
        return userEntity;
    }

    /**
     * 获取用户全部概要
     * @param username 用户名
     * @param month 月份
     * @return 概要
     */
    public List<SummaryEntity> getSummaries(String username, Date month) {
        Date start = DateTool.getSummaryStartDate(month);
        Date end = DateTool.getSummaryEndDate(month);
        return summaryRepo.findByUsernameAndDateBetween(username, start, end);
    }

    /**
     * 新建申辩
     * @param id 概要ID
     * @param username 申请用户名
     * @param type 申辩类型
     * @return 是否成功
     */
    public boolean newAverment(int id, String username, AvermentType type) {
        SummaryEntity summaryEntity = summaryRepo.findOne(id);
        if (summaryEntity.getUsername().equals(username)) {
            AvermentEntity avermentEntity = new AvermentEntity();
            avermentEntity.setSummary(summaryEntity);
            avermentEntity.setType(type);
            avermentEntity.setStatus(AvermentStatus.WAIT);
            avermentRepo.save(avermentEntity);
            summaryEntity.setStatus(SummaryStatus.WAIT);
            summaryRepo.save(summaryEntity);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取全部用户概要
     * @param month 月份
     * @return 概要和用户信息
     */
    public List<AllSummary> getAllSummaries(Date month) {
        List<AllSummary> allSummaryList = new ArrayList<>();
        List<EmployeeEntity> employeeEntityList = IterableUtils.toList(employeeRepo.findAll());
        Date start = DateTool.getSummaryStartDate(month);
        Date end = DateTool.getSummaryEndDate(month);
        for (EmployeeEntity employeeEntity: employeeEntityList) {
            AllSummary allSummary = new AllSummary();
            allSummary.setSummaries(summaryRepo.findByUsernameAndDateBetween(employeeEntity.getUsername(), start, end));
            allSummary.setUserInfo(getUserInfo(employeeEntity.getUsername()));
            allSummaryList.add(allSummary);
        }
        return allSummaryList;
    }

    /**
     * 获取申辩
     * @param status 申辩状态
     * @return 申辩列表
     */
    public List<AllAverment> getAverments(AvermentStatus status) {
        List<AllAverment> allAvermentList = new ArrayList<>();
        List<AvermentEntity> avermentEntityList = avermentRepo.findByStatus(status);
        for (AvermentEntity avermentEntity : avermentEntityList) {
            AllAverment allAverment = new AllAverment();
            allAverment.setAverment(avermentEntity);
            allAverment.setUserInfo(getUserInfo(avermentEntity.getSummary().getUsername()));
            allAvermentList.add(allAverment);
        }
        return allAvermentList;
    }

    /**
     * 更新申辩
     * @param id 申辩ID
     * @param isApprove 是否通过
     * @param operator 操作人
     * @return 是否成功
     */
    public boolean updateAverment(int id, boolean isApprove, String operator) {
        AvermentEntity avermentEntity = avermentRepo.findOne(id);
        if (null == avermentEntity || avermentEntity.getStatus() != AvermentStatus.WAIT) return false;
        avermentEntity.setOperator(operator);
        if (isApprove) {
            // 通过
            avermentEntity.setStatus(AvermentStatus.PASS);
            avermentEntity.getSummary().setWork(true);
            avermentEntity.getSummary().setStatus(SummaryStatus.PASS);
            switch (avermentEntity.getType()) {
                case VACATE:
                case ERRAND:
                case BUG:
                    avermentEntity.getSummary().setLateTime(0);
                    avermentEntity.getSummary().setAfterTime(0);
                    break;
                case LATE:
                    avermentEntity.getSummary().setLateTime(30);
                    avermentEntity.getSummary().setAfterTime(30);
                    break;
            }
        } else {
            // 不通过
            avermentEntity.setStatus(AvermentStatus.REJECT);
        }
        avermentRepo.save(avermentEntity);
        return true;
    }

    /**
     * Roll 点
     * @param userEntity 用户
     * @param type 类型
     */
    public RollEntity roll(UserEntity userEntity, RollType type) {
        // 对研发Roll点做特判，只允许研发组和测试组
        if (type == RollType.SHARE && userEntity.getEmployee().getSeries() != EmployeeSeries.DEVELOP
            && userEntity.getEmployee().getSeries() != EmployeeSeries.TEST) {
            return null;
        }

        RollEntity rollEntity = rollRepo.findByEmployeeAndDateAndType(userEntity.getEmployee(), DateTool.getToday(), type);
        if (null == rollEntity) {
            rollEntity = new RollEntity();
            rollEntity.setEmployee(userEntity.getEmployee());
            rollEntity.setDate(DateTool.getToday());
            Random random = new Random();
            int roll = random.nextInt(MAX_ROLL);
            // 避免重复
            while (null != rollRepo.findByDateAndTypeAndRoll(DateTool.getToday(), type, roll)) {
                roll = random.nextInt(MAX_ROLL);
            }
            rollEntity.setRoll(roll);
            rollEntity.setType(type);
            rollRepo.save(rollEntity);
            return rollEntity;
        }  else {
            return null;
        }
    }

    public List<RollEntity> getRoll(Date date, RollType type) {
        return rollRepo.findByDateAndType(date, type);
    }

    public UserInfo getUserInfo(String username) {
        EmployeeEntity employeeEntity = employeeRepo.findOne(username);
        if (null == employeeEntity) return null;
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(employeeEntity.getUsername());
        userInfo.setJid(employeeEntity.getJid());
        userInfo.setName(employeeEntity.getName());
        userInfo.setPunish(employeeEntity.getPunish());
        userInfo.setInternship(employeeEntity.isInternship());
        userInfo.setAdmin(employeeEntity.isAdmin());
        return userInfo;
    }
}
