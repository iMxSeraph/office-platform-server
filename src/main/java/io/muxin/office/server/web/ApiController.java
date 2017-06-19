package io.muxin.office.server.web;

import io.muxin.office.server.common.constant.AuthType;
import io.muxin.office.server.common.constant.AvermentStatus;
import io.muxin.office.server.common.constant.ConstValue;
import io.muxin.office.server.config.annotation.Authentication;
import io.muxin.office.server.config.annotation.CurrentUser;
import io.muxin.office.server.dto.api.request.Account;
import io.muxin.office.server.dto.api.request.NewAverment;
import io.muxin.office.server.dto.api.request.UpdateAverment;
import io.muxin.office.server.dto.api.response.AllAverment;
import io.muxin.office.server.dto.api.response.AllSummary;
import io.muxin.office.server.dto.api.response.BaseResponse;
import io.muxin.office.server.dto.api.response.UserInfo;
import io.muxin.office.server.entity.mysql.*;
import io.muxin.office.server.entity.redis.TokenEntity;
import io.muxin.office.server.service.SignService;
import io.muxin.office.server.service.TokenService;
import io.muxin.office.server.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 接口控制类
 * Created by muxin on 2017/2/23.
 */
@RestController
@RequestMapping(path = "/api")
public class ApiController {

    private TokenService tokenService;

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    private WebService webService;

    @Autowired
    public void setWebService(WebService webService) {
        this.webService = webService;
    }

    private SignService signService;

    @Autowired
    public void setSignService(SignService signService) {
        this.signService = signService;
    }

    /**
     * 登陆接口
     * @param request 登陆请求体
     * @return 返回包
     */
    @RequestMapping(path = "/tokens", method = RequestMethod.POST)
    public BaseResponse login(@RequestBody Account request) {
        BaseResponse response = new BaseResponse();
        UserEntity userEntity = webService.login(request.getUsername(), request.getPassword());
        if (null != userEntity) {
            TokenEntity tokenEntity = tokenService.createToken(request.getUsername());
            response.setMessage(tokenEntity.getToken());
        } else {
            response.setError("用户名/密码错误");
        }
        return response;
    }

    /**
     * 注册接口
     * @param request 注册账号请求体
     * @return 返回包
     */
    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public BaseResponse register(@RequestBody Account request) {
        BaseResponse response = new BaseResponse();
        UserEntity userEntity = webService.register(request.getUsername(), request.getPassword());
        if (null != userEntity) {
            response.setMessage("注册成功");
        } else {
            response.setError("注册失败：用户已存在或用户不在可注册范围");
        }
        return response;
    }

    /**
     * 登出接口
     * @param request 请求体
     */
    @Authentication
    @RequestMapping(path = "/tokens", method = RequestMethod.DELETE)
    public void logout(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, ConstValue.TOKEN_HEADER);
        TokenEntity tokenEntity = tokenService.getToken(cookie.getValue());
        tokenService.destroyToken(tokenEntity.getToken());
    }

    /**
     * 获取用户基本信息
     * @param userEntity 自动装配用户信息
     * @return 用户信息
     */
    @Authentication
    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public UserInfo getUser(@CurrentUser UserEntity userEntity) {
        return webService.getUserInfo(userEntity.getUsername());
    }

    /**
     * 获取用户概要信息
     * @param userEntity 自动装配用户信息
     * @param month 请求月份
     * @return 概要信息
     */
    @Authentication
    @RequestMapping(path = "/summaries/{month}", method = RequestMethod.GET)
    public List<SummaryEntity> getSummaries(@CurrentUser UserEntity userEntity, @PathVariable @DateTimeFormat(pattern = "yyyyMM") Date month) {
        return webService.getSummaries(userEntity.getUsername(), month);
    }

    /**
     * 新建申辩
     * @param request 请求体
     * @param userEntity 自动装配用户信息
     * @return 返回包
     */
    @Authentication
    @RequestMapping(path = "/averments", method = RequestMethod.POST)
    public BaseResponse newAverment(@RequestBody NewAverment request, @CurrentUser UserEntity userEntity) {
        if (webService.newAverment(request.getId(), userEntity.getUsername(), request.getType())) {
            return BaseResponse.SUCCESS;
        } else {
            return BaseResponse.FAIL;
        }
    }

    /**
     * 获取全部用户签到信息
     * @param month 请求月份
     * @return 全部用户信息
     */
    @Authentication(AuthType.ADMIN)
    @RequestMapping(path = "/summaries/all/{month}", method = RequestMethod.GET)
    public List<AllSummary> getAllSummaries(@PathVariable @DateTimeFormat(pattern = "yyyyMM") Date month) {
        return webService.getAllSummaries(month);
    }

    /**
     * 获取全部待批准申辩
     * @return 全部待批准申辩
     */
    @Authentication(AuthType.ADMIN)
    @RequestMapping(path = "/averments/{status}", method = RequestMethod.GET)
    public List<AllAverment> getAverments(@PathVariable AvermentStatus status) {
        return webService.getAverments(status);
    }

    /**
     * 更新申辩批准
     * @return 更新结果
     */
    @Authentication(AuthType.ADMIN)
    @RequestMapping(path = "/averments/{id}", method = RequestMethod.PUT)
    public BaseResponse updateAverment(@RequestBody UpdateAverment request, @CurrentUser UserEntity userEntity, @PathVariable int id) {
        if (webService.updateAverment(id, request.isApprove(), userEntity.getUsername())) {
            return BaseResponse.SUCCESS;
        } else {
            return BaseResponse.FAIL;
        }
    }

    /**
     * 手动计算考勤
     * @return 成功
     */
    @Authentication(AuthType.ADMIN)
    @RequestMapping("/manual/do/{startDate}/{endDate}")
    public BaseResponse doSummary(@PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date startDate, @PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date endDate) {
        signService.doSummary(startDate, endDate);
        return BaseResponse.SUCCESS;
    }

    /**
     * 手动计算考勤
     * @return 成功
     */
    @Authentication(AuthType.ADMIN)
    @RequestMapping("/manual/update/{startDate}/{endDate}")
    public BaseResponse updateSummary(@PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date startDate, @PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date endDate) {
        signService.updateSummary(startDate, endDate);
        return BaseResponse.SUCCESS;
    }
}
