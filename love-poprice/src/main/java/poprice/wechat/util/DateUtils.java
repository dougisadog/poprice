package poprice.wechat.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    //当天的
    public static String getNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        Date monday = c.getTime();
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    //当天的 前七天
    public static String getStatetime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -6);
        Date monday = c.getTime();
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    //当天的 前七天
    public static String getLastMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -30);
        Date monday = c.getTime();
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    public static String getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, +1);
        Date monday = c.getTime();
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    //获得时间戳
    public static String geTimeStamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return  sdf.format(date);
    }

}
