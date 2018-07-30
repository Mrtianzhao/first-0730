package net.dgg.system.util;

import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/11/20.
 */
public class Tools {

    /** 手机号 */
    public static final String REG_PHONE = "^[1][3,4,5,7,8][0-9]{9}$";

    /** 座机号 */
    public static final String REG_TEL = "^\\d{6}$";

    /**
     * 校验是否是手机号
     *
     * @param number 号码
     * @return 校验结果
     */
    public static boolean isMobile(String number) {
        boolean isOk = false;
        String regPhone = REG_PHONE;
        try {
            // 验证手机号
            Pattern p = Pattern.compile(regPhone);
            Matcher m = p.matcher(number);
            isOk = m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOk;
    }

    /**
     * 校验是否为6位数字
     *
     * @param str 字符
     * @return 结果
     */
    public static boolean isSixNumber(String str) {
        boolean isOk = false;
        String regTel = REG_TEL;
        try {
            // 验证手机号
            Pattern p = Pattern.compile(regTel);
            Matcher m = p.matcher(str);
            isOk = m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOk;
    }

    /**
     * 获取系统当前时间:yyyy-MM-DD HH:mm:ss
     *
     * @return 当前时间
     */
    public static String getCurrentTime() {
        return String.format("%1$tF %1$tT", new Date());
    }

    /**
     * 号码脱敏处理
     *
     * @param accnbr 号码
     * @return 脱敏号码
     */
    public static String packageAccNbr(String accnbr) {
        //判断是否手机号
        if (Tools.verifyUserTel(accnbr)) {
            //手机号脱敏
            return accnbr.substring(0, 3) + "****" + accnbr.substring(7, accnbr.length());
        } else {
            //不是手机号脱敏
            return accnbr.substring(0, 2) + "****" + accnbr.substring(6, accnbr.length());
        }
    }

    /**
     * 判断是否手机号
     *
     * @param userTel
     * @return 返回boolean值
     */
    public static boolean verifyUserTel(String userTel) {
        String regExp = "^[1][3|4|5|7|8]\\d{9}$";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(userTel);
        return matcher.matches();
    }

    /**
     * 随机生成数字验证码
     * @param num
     * @return
     */
    public static String genMsgCode(int num) {
        Random random = new Random();
        StringBuffer messCode = new StringBuffer();

        for (int i = 0; i < num; i++) {
            messCode.append(random.nextInt(10));
        }
        return messCode.toString();
    }

}
