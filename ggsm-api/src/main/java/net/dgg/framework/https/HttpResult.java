package net.dgg.framework.https;
/**
 * <p>@Title http返回</p>
 * <p>@Description </p>
 * <p>@Version 1.0.0</p>
 * <p>@author rebin</p>
 * <p>@date 2017年7月20日</p>
 * <p>xiefangjian@163.com</p>
 * <p>Copyright © Noka Laboratory.All Rights Reserved.</p>
 */
public class HttpResult {
	private Integer code;
    private String data;

    public HttpResult(Integer code, String data) {
        this.code = code;
        this.data = data;
    }

    public HttpResult() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
