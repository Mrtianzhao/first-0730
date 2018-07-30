package net.dgg.framework.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by wu on 2017/8/3.
 */
public class UUIDUtils {

    public static String u32(){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        UUID uuid = UUID.randomUUID();
        String guidStr = uuid.toString();
        md.update(guidStr.getBytes(), 0, guidStr.length());
        return new BigInteger(1, md.digest()).toString(32).toUpperCase();
    }

    public static void main(String[] args){
        System.out.println(UUIDUtils.u32());
    }
}
