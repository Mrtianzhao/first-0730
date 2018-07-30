package net.dgg.framework.utils;

import java.util.UUID;

/**
 * Created by wu on 2017/7/26.
 */
public class PrimaryKeyUtils {

    public static String getId(){
    	return UUID.randomUUID().toString();
    }

}
