package io.muxin.office.server.web;

import io.muxin.office.server.service.TokenService;
import io.muxin.office.server.service.AsyncService;
import io.muxin.office.server.service.SignService;
import io.muxin.office.server.service.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 测试类
 * Created by muxin on 2017/2/23.
 */
@RestController
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    private HttpService httpService;

    @Autowired
    public void setHttpService(HttpService httpService) {
        this.httpService = httpService;
    }

    @Autowired
    private AsyncService asyncService;
    @Autowired
    private SignService signService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping("/api/test/{startDate}/{endDate}")
    public String test(@PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date startDate, @PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date endDate) throws Exception {
//        Process exeEcho = Runtime.getRuntime().exec("nmap -sP 192.168.1.0/24");
//        return IOUtils.toString(exeEcho.getInputStream());
//        coreService.doSignIn("DC:A4:CA:11:52:6a", new Date());
        signService.updateSummary(startDate, endDate);
//        signService.updateSummary(calendar.getTime(), calendar.getTime());
//        Hashtable<String, String> env = new Hashtable<>();
//        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//        env.put(Context.PROVIDER_URL, "LDAP://iflytek.com");
//        env.put(Context.SECURITY_PRINCIPAL, "mxfang@iflytek.com");
//        env.put(Context.SECURITY_CREDENTIALS, "123456");
//        new InitialDirContext(env);

        return "ok";
    }
}
