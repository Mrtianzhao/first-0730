package net.dgg.system.dto;

/**
 * @author ytz
 * @date 2018/4/28.
 * @desc 登录对象
 */
public class LoginDto {

    /** 手机号 */
    private String phoneNo;

    /** 图片验证码 */
    private String code;

    /** 短信验证码 */
    private String msgCode;

    /** token */
    private String token;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
