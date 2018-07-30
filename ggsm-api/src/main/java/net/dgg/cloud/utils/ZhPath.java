package net.dgg.cloud.utils;

/**
 * Created by Enzo Cotter on 2018/5/17.
 */
public class ZhPath {
    //4位坐席号
    private static  String num_1 = "4,5";
    private static  String num_2 = "6,7";
    private static  String num_5 = "1";
    private static  String num_7 = "2";
    private static  String num_9 = "3";
    private static String num_27 = "9";

    private static  String num_3 = "13";
    private static  String num_4 = "41";
    private static  String num_6 = "43";
    private static  String num_8 = "42";
    private static  String num_10 = "44";
    private static  String num_11 = "47";
    private static  String num_12 = "38";
    private static String num_13 = "36";
    private static String num_14 = "37";
    private static String num_15 = "40";
    private static String num_16 = "46";
    private static String num_17 = "22";
    private static String num_18 = "16";
    private static String num_19 = "30";
    private static String num_20 = "17";
    private static String num_21 = "28";
    private static String num_22 = "39";
    private static String num_23 = "14";
    private static String num_24 = "20";
    private static String num_25 = "19";
    private static String num_26 = "29";
    private static String num_28 = "31";
    private static String num_29 = "32";


    private static String endpoint_ip_1 = "172.16.25.1";
    private static String endpoint_ip_2 = "192.168.254.77";
    private static String endpoint_ip_3 = "192.168.254.77";
    private static String endpoint_ip_4 = "172.16.94.252";
    private static String endpoint_ip_5 = "172.16.56.3";
    private static String endpoint_ip_6 = "172.16.97.10";
    private static String endpoint_ip_7 = "172.16.48.250";
    private static String endpoint_ip_8 = "172.16.112.6";
    private static String endpoint_ip_9 = "172.16.32.41";
    private static String endpoint_ip_10 = "192.168.254.44";
    private static String endpoint_ip_11 = "192.168.254.47";
    private static String endpoint_ip_12 = "192.168.254.38";
    private static String endpoint_ip_13 = "192.168.254.36";
    private static String endpoint_ip_14 = "192.168.254.37";
    private static String endpoint_ip_15 = "192.168.254.40";
    private static String endpoint_ip_16 = "192.168.1.100";
    private static String endpoint_ip_17 = "192.168.1.101";
    private static String endpoint_ip_18 = "192.168.1.102";
    private static String endpoint_ip_19 = "172.16.128.5";
    private static String endpoint_ip_20 = "192.168.254.17";
    private static String endpoint_ip_21 = "192.168.254.28";
    private static String endpoint_ip_22 = "192.168.254.39";
    private static String endpoint_ip_23 = "192.168.254.14";
    private static String endpoint_ip_24 = "192.168.254.20";
    private static String endpoint_ip_25 = "192.168.254.19";
    private static String endpoint_ip_26 = "192.168.254.29";
    private static String endpoint_ip_27 = "192.168.254.9";
    private static String endpoint_ip_28 = "172.16.150.2";
    private static String endpoint_ip_29 = "172.16.166.2";


    private static String endpoint_1 = "bjpbx.dgg.net";
    private static String endpoint_2 = "cdpbx.dgg.net";
    private static String endpoint_3 = "cdpbx.dgg.net";
    private static String endpoint_4 = "whpbx.dgg.net";
    private static String endpoint_5 = "cqpbx.dgg.net";
    private static String endpoint_6 = "szpbx.dgg.net";
    private static String endpoint_7 = "gzpbx.dgg.net";
    private static String endpoint_8 = "hzpbx.dgg.net";
    private static String endpoint_9 = "szhrpbx.dgg.net";
    private static String endpoint_10 = "cdrzpbx.dgg.net";
    private static String endpoint_11 = "xdjzpbx.dgg.net";
    private static String endpoint_12 = "cd2pbx.dgg.net";
    private static String endpoint_13 = "cd36pbx.dgg.net";
    private static String endpoint_14 = "cd37pbx.dgg.net";
    private static String endpoint_15 = "cd40pbx.dgg.net";
    private static String endpoint_16 = "cd46pbx.dgg.net";
    private static String endpoint_17 = "cd22pbx.dgg.net";
    private static String endpoint_18 = "cd16pbx.dgg.net";
    private static String endpoint_19 = "cd30pbx.dgg.net";
    private static String endpoint_20 = "cd17pbx.dgg.net";
    private static String endpoint_21 = "cd28pbx.dgg.net";
    private static String endpoint_22 = "cd39pbx.dgg.net";
    private static String endpoint_23 = "cd14pbx.dgg.net";
    private static String endpoint_24 = "cd20pbx.dgg.net";
    private static String endpoint_25 = "cd19pbx.dgg.net";
    private static String endpoint_26 = "cd29pbx.dgg.net";
    private static String endpoint_27 = "cd9pbx.dgg.net";
    private static String endpoint_28 = "zz31pbx.dgg.net";
    private static String endpoint_29 = "fs32pbx.dgg.net";

    public static String getAllPlayPath(String seatNumber,String record){
        String fileUrl = "/api/record/?action=play&file="+record;
        String allPath = getZhPath(seatNumber)+fileUrl;
        return allPath;
    }
    
    public static  String getZhPath(String seatNumber){
        String url = "";

        if(seatNumber.length() < 5){
            seatNumber = seatNumber.substring(0,1);
            if(num_1.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_1;
            }else if(num_2.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_2;
            }else if(num_5.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_5;
            }else if(num_9.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_9;
            }else if(num_7.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_7;
            }else if(num_27.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_27;
            }
        }else{
            seatNumber = seatNumber.substring(0,2);
            if(num_3.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_3;
            }else if(num_13.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_13;
            }else if(num_14.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_14;
            }else if(num_12.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_12;
            }else if(num_15.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_15;
            }else if(num_4.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_4;
            }else if(num_8.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_8;
            }else if(num_16.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_16;
            }else if(num_17.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_17;
            }else if(num_18.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_18;
            }else if(num_19.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_19;
            }else if(num_20.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_20;
            }else if(num_21.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_21;
            }else if(num_22.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_22;
            }else if(num_23.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_23;
            }else if(num_24.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_24;
            }else if(num_25.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_25;
            }else if(num_26.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_26;
            }else if(num_28.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_28;
            }else if(num_6.indexOf(seatNumber) != -1){
                url  = "http://"+endpoint_6;
            }
        }
        return url;
    }
}
