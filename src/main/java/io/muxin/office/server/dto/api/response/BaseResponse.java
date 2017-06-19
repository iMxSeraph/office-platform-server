package io.muxin.office.server.dto.api.response;

/**
 * 基础返回内容
 * Created by muxin on 2017/2/24.
 */
public class BaseResponse {

    private String message;

    private String error;

    public BaseResponse() {
    }

    public BaseResponse(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static BaseResponse SUCCESS = new BaseResponse("成功", null);

    public static BaseResponse FAIL = new BaseResponse(null, "失败");
}
