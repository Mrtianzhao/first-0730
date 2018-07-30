package net.dgg.cloud.utils;

/**
 * @Author zhangyuzhu
 * @Description 自定义运行时异常
 * @Created data 2018/7/13 10:38
 */
public class DGGException extends RuntimeException{
    public DGGException() {
        super();
    }

    public DGGException(String msg){
        super(msg);
    }
}
