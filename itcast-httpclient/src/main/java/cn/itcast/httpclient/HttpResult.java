package cn.itcast.httpclient;
/*
* 返回的响应实体类
*
* */
public class HttpResult {
    private  Integer code;//响应的状态码 200、201
    private String body;//响应体 （响应内容）

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public HttpResult(Integer code, String body) {
        this.code = code;
        this.body = body;
    }
    public HttpResult() {

    }

}
