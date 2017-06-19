package io.muxin.office.server.service;

import io.muxin.office.server.common.constant.SummaryStatus;
import io.muxin.office.server.entity.mssql.CheckInOutEntity;
import io.muxin.office.server.entity.mysql.*;
import io.muxin.office.server.repo.mssql.CheckInOutRepo;
import io.muxin.office.server.repo.mysql.*;
import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 签到服务类
 * Created by muxin on 2017/2/23.
 */
@Service
public class SignService {

    private static final Logger logger = LoggerFactory.getLogger(SignService.class);

    private DeviceRepo deviceRepo;

    @Autowired
    public void setDeviceRepo(DeviceRepo deviceRepo) {
        this.deviceRepo = deviceRepo;
    }

    private RecordRepo recordRepo;

    @Autowired
    public void setRecordRepo(RecordRepo recordRepo) {
        this.recordRepo = recordRepo;
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

    private CheckInOutRepo checkInOutRepo;

    @Autowired
    public void setCheckInOutRepo(CheckInOutRepo checkInOutRepo) {
        this.checkInOutRepo = checkInOutRepo;
    }

    private AsyncService asyncService;

    @Autowired
    public void setAsyncService(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    private AvermentRepo avermentRepo;

    @Autowired
    public void setAvermentRepo(AvermentRepo avermentRepo) {
        this.avermentRepo = avermentRepo;
    }

    private WorkdayRepo workdayRepo;

    @Autowired
    public void setWorkdayRepo(WorkdayRepo workdayRepo) {
        this.workdayRepo = workdayRepo;
    }

    /**
     * 执行签到
     * @param macs MAC 地址扫描列表
     */
    public void doSignIn(String macs, Date now) throws Exception {
        // 获取全部设备
        List<DeviceEntity> deviceEntityList = IterableUtils.toList(deviceRepo.findAll());
        for (DeviceEntity deviceEntity : deviceEntityList) {
            // 比对 MAC 地址
            if (macs.toUpperCase().contains(deviceEntity.getMacAddress())) {
                // 查询今日签到记录
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                Date start = calendar.getTime();
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
                Date end = calendar.getTime();
                List<RecordEntity> recordEntityList = recordRepo.findByUsernameAndRecordBetween(deviceEntity.getUsername(), start, end);
                RecordEntity recordEntity;
                if (recordEntityList.size() < 2) {
                    // 如果第一次签到发送通知
                    if (recordEntityList.size() == 0) {
                        logger.info(deviceEntity.getUsername() + "签到成功");
                        asyncService.notifySignIn(deviceEntity.getUsername(), now);
                    }
                    recordEntity = new RecordEntity();
                    recordEntity.setUsername(deviceEntity.getUsername());
                    recordEntity.setRecord(now);
                    recordRepo.save(recordEntity);
                } else {
                    recordEntity = recordEntityList.get(recordEntityList.size() - 1);
                    recordEntity.setRecord(now);
                    recordRepo.save(recordEntity);
                }
            }
        }
    }

    /**
     * 计算指定区间的签到情况
     * @param start 开始时间（闭）
     * @param end 结束时间（闭）
     */
    public void doSummary(Date start, Date end) {
        Calendar now = Calendar.getInstance();
        // 设置日期并去除时间部分
        now.setTime(start);
        now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

        // 获取全部User列表
        List<EmployeeEntity> employeeEntityList = IterableUtils.toList(employeeRepo.findAll());
        // 遍历区间
        while (!now.getTime().after(end)) {
            // 除去休息日
            if (isWork(now)) {
                // 遍历每个人
                for (EmployeeEntity employeeEntity : employeeEntityList) {
                    SummaryEntity summaryEntity = summaryRepo.findByUsernameAndDate(employeeEntity.getUsername(), now.getTime());
                    // 如果无签到记录进行新增，有则不处理
                    if (null == summaryEntity) {
                        summaryEntity = new SummaryEntity();
                        summaryEntity.setStatus(SummaryStatus.NONE);
                        summaryEntity.setUsername(employeeEntity.getUsername());
                        summaryEntity.setDate(now.getTime());
                        // 今日签到开始时间
                        Calendar startTime = Calendar.getInstance();
                        startTime.setTime(now.getTime());
                        // 今日签到结束时间
                        Calendar endTime = Calendar.getInstance();
                        endTime.setTime(now.getTime());
                        endTime.set(endTime.get(Calendar.YEAR), endTime.get(Calendar.MONTH), endTime.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
                        // 取出今日签到记录
                        List<RecordEntity> recordEntityList = recordRepo.findByUsernameAndRecordBetween(employeeEntity.getUsername(), startTime.getTime(), endTime.getTime());
                        if (recordEntityList.size() == 0) {
                            // 无签到记录
                            summaryEntity.setWork(false);
                        } else {
                            // 有签到记录
                            summaryEntity.setWork(true);
                            Date signInRecord = recordEntityList.get(0).getRecord();
                            Date signOutRecord = recordEntityList.get(recordEntityList.size() - 1).getRecord();
                            if (employeeEntity.isInternship()) {
                                processInternshipMember(summaryEntity, signInRecord);
                            } else {
                                processOfficialMember(summaryEntity, signInRecord, signOutRecord, employeeEntity.getPunish());
                            }
                        }
                        summaryRepo.save(summaryEntity);
                    }
                }
            }
            now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 1);
        }
    }

    /**
     * 根据签到机更新考勤数据
     * @param start
     * @param end
     */
    public void updateSummary(Date start, Date end) {
        Calendar now = Calendar.getInstance();
        // 设置日期并去除时间部分
        now.setTime(start);
        now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

        // 获取全部User列表
        List<EmployeeEntity> employeeEntityList = IterableUtils.toList(employeeRepo.findAll());
        // 遍历区间
        while (!now.getTime().after(end)) {
            // 除去休息日
            if (isWork(now)) {
                // 遍历每个人
                for (EmployeeEntity employeeEntity : employeeEntityList) {
                    // 今日签到开始时间
                    Calendar startTime = Calendar.getInstance();
                    startTime.setTime(now.getTime());
                    // 今日签到结束时间
                    Calendar endTime = Calendar.getInstance();
                    endTime.setTime(now.getTime());
                    endTime.set(endTime.get(Calendar.YEAR), endTime.get(Calendar.MONTH), endTime.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
                    // 取出今日考勤机签到记录
                    List<CheckInOutEntity> checkInOutEntityList = checkInOutRepo.getRecord(employeeEntity.getId(), startTime.getTime(), endTime.getTime());
                    // 如果考勤机有签到记录进行计算，如果没有则不计算
                    if (checkInOutEntityList.size() > 0) {
                        SummaryEntity summaryEntity = summaryRepo.findByUsernameAndDate(employeeEntity.getUsername(), now.getTime());
                        // 将签到记录取出，并将秒进位
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(checkInOutEntityList.get(0).getKey().getCheckTime());
                        if (calendar.get(Calendar.SECOND) > 0) {
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 1);
                        }
                        Date signInRecord = calendar.getTime();
                        calendar.setTime(checkInOutEntityList.get(checkInOutEntityList.size() - 1).getKey().getCheckTime());
                        calendar.set(Calendar.SECOND, 0);
                        if (calendar.get(Calendar.SECOND) > 0) {
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 1);
                        }
                        Date signOutRecord = calendar.getTime();
                        if (summaryEntity.getStatus() == SummaryStatus.NONE) {
                            // 如果还未申辩申辩，则开始更新
                            if (!summaryEntity.isWork()) {
                                // 如果有考勤记录但是未到岗，则完全用考勤机数据
                                summaryEntity.setWork(true);
                                if (employeeEntity.isInternship()) {
                                    processInternshipMember(summaryEntity, signInRecord);
                                } else {
                                    processOfficialMember(summaryEntity, signInRecord, signOutRecord, employeeEntity.getPunish());
                                }
                            } else {
                                // 如果已有考勤记录，则进行更新
                                if (employeeEntity.isInternship()) {
                                    updateInternshipMember(summaryEntity, signInRecord);
                                } else {
                                    updateOfficialMember(summaryEntity, signInRecord, signOutRecord, employeeEntity.getPunish());
                                }
                            }
                            summaryRepo.save(summaryEntity);
                        }
                    }
                }
            }
            now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 1);
        }
    }

    /**
     * 处理实习生考勤
     * @param summaryEntity 概要
     * @param signInRecord 签到时间
     */
    private void processInternshipMember(SummaryEntity summaryEntity, Date signInRecord) {
        summaryEntity.setArriveTime(signInRecord);
    }

    /**
     * 处理正式员工考勤
     * @param summaryEntity 概要
     * @param signInRecord 签到时间
     * @param signOutRecord 签退时间
     * @param punish 惩罚
     */
    private void processOfficialMember(SummaryEntity summaryEntity, Date signInRecord, Date signOutRecord, int punish) {
        summaryEntity.setArriveTime(signInRecord);
        summaryEntity.setLeftTime(signOutRecord);
        // 将 Date 转为 Calendar
        Calendar signIn = Calendar.getInstance();
        signIn.setTime(signInRecord);
        Calendar signOut = Calendar.getInstance();
        signOut.setTime(signOutRecord);
        // 开始计算时间
        calculateSummary(summaryEntity, signIn, signOut, punish);
    }

    /**
     * 更新实习生考勤
     * @param summaryEntity 概要
     * @param signInRecord 签到时间
     */
    private void updateInternshipMember(SummaryEntity summaryEntity, Date signInRecord) {
        if (signInRecord.before(summaryEntity.getArriveTime())) {
            summaryEntity.setArriveTime(signInRecord);
        }
    }

    /**
     * 更新正式员工考勤
     * @param summaryEntity 概要
     * @param signInRecord 签到时间
     * @param signOutRecord 签退时间
     * @param punish 惩罚
     */
    private void updateOfficialMember(SummaryEntity summaryEntity, Date signInRecord, Date signOutRecord, int punish) {
        boolean flag = false;
        if (signInRecord.before(summaryEntity.getArriveTime())) {
            summaryEntity.setArriveTime(signInRecord);
            flag = true;
        }
        if (signOutRecord.after(summaryEntity.getLeftTime())) {
            summaryEntity.setLeftTime(signOutRecord);
            flag = true;
        }
        if (flag) {
            // 将 Date 转为 Calendar
            Calendar signIn = Calendar.getInstance();
            signIn.setTime(signInRecord);
            Calendar signOut = Calendar.getInstance();
            signOut.setTime(signOutRecord);
            calculateSummary(summaryEntity, signIn, signOut, punish);
        }
    }

    /**
     * 计算考勤中的各项时间
     * @param summaryEntity 概要
     * @param signIn 签到时间
     * @param signOut 签退时间
     * @param punish 惩罚
     */
    private void calculateSummary(SummaryEntity summaryEntity, Calendar signIn, Calendar signOut, int punish) {
        // 开始计算时间
        summaryEntity.setLateTime(Math.max((signIn.get(Calendar.HOUR_OF_DAY) - 9) * 60 + (signIn.get(Calendar.MINUTE) - (30 - punish)), 0));
        // 如果迟到则从 9 点开始计算时间
        if (summaryEntity.getLateTime() > 0) {
            summaryEntity.setLateTime(summaryEntity.getLateTime() + (30 - punish));
        }
        summaryEntity.setAfterTime(Math.max((signIn.get(Calendar.HOUR_OF_DAY) - 9) * 60 + signIn.get(Calendar.MINUTE), 0));
        summaryEntity.setEarlyTime(Math.max(((9 - signIn.get(Calendar.HOUR_OF_DAY)) * 60 + (0 - signIn.get(Calendar.MINUTE))), 0));
        int overTime = 0;
        overTime += Math.max(((8 - signIn.get(Calendar.HOUR_OF_DAY)) * 60 + (50 - signIn.get(Calendar.MINUTE))), 0);
        overTime += Math.max(((signOut.get(Calendar.HOUR_OF_DAY) - 18) * 60 + signOut.get(Calendar.MINUTE)), 0);
        summaryEntity.setOverTime(overTime);
    }

    /**
     * 判断今天是否工作
     * @param now 现在日期
     * @return 是否工作
     */
    private boolean isWork(Calendar now) {
        WorkdayEntity workdayEntity = workdayRepo.findOne(now.getTime());
        if (null != workdayEntity) {
            return workdayEntity.isWork();
        } else {
            return now.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && now.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY;
        }
    }
}
