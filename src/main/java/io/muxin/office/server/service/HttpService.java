package io.muxin.office.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.muxin.office.server.common.constant.ConstValue;
import io.muxin.office.server.dto.wechat.AccessTokenResponse;
import io.muxin.office.server.dto.wechat.message.TextMessage;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * HTTP 请求服务类
 * Created by muxin on 2017/2/24.
 */
@Service
public class HttpService {

    private static final Logger logger = LoggerFactory.getLogger(HttpService.class);

    private HttpClientBuilder clientBuilder;
    private ObjectMapper objectMapper;

    private DataService dataService;

    @Autowired
    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public HttpService() {
        PoolingHttpClientConnectionManager cm;
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(ConstValue.MAX_CONNECTIONS);
        cm.setDefaultMaxPerRoute(ConstValue.MAX_CONNECTIONS);

        clientBuilder = HttpClients.custom();
        clientBuilder.setConnectionManager(cm);
        clientBuilder.setConnectionManagerShared(true);

        objectMapper = new ObjectMapper();
    }

    /**
     * 执行 POST 请求
     * @param url 请求地址
     * @return 返回内容
     * @throws Exception 异常
     */
    public String doPost(String url, String data) throws Exception {
        CloseableHttpClient httpClient = clientBuilder.build();
        HttpPost request = new HttpPost(url);
        StringEntity entity = new StringEntity(data, StandardCharsets.UTF_8);
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);
        String result = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        httpClient.close();
        return result;
    }

    /**
     * 执行GET请求
     * @param url 请求地址
     * @return 返回内容
     * @throws Exception 异常
     */
    public String doGet(String url) throws Exception {
        CloseableHttpClient httpClient = clientBuilder.build();
        HttpGet request = new HttpGet(url);
        HttpResponse response = httpClient.execute(request);
        String result = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        httpClient.close();
        return result;
    }

    /**
     * 更新AccessToken
     * @throws Exception 异常
     */
    public void updateAccessToken() throws Exception {
        AccessTokenResponse response = objectMapper.readValue(doGet(ConstValue.ACCESS_TOKEN_URL), AccessTokenResponse.class);
        dataService.setAccessToken(response.getAccssToken());
        logger.info("更新 AccessToken 成功：" + response.getAccssToken());
    }

    /**
     * 发送消息
     * @param message 消息体
     */
    public void sendMessage(TextMessage message) throws Exception {
        String url = StringUtils.replace(ConstValue.SEND_MESSAGE_URL, "${ACCESS_TOKEN}", dataService.getAccessToken());
        doPost(url, objectMapper.writeValueAsString(message));
    }
}
