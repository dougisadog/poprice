package poprice.wechat;

import com.google.common.collect.Maps;

import poprice.wechat.domain.base.Announcement;
import poprice.wechat.util.OSInfoUtils;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 枚举。里面定义静态的enum给domain字段使用, 以及定义常量。
 */
public final class Constants {
    public static DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter FORMAT_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";//本地
    public static final String SPRING_PROFILE_PRODUCTION = "prod";//服务器
    public static final String SYSTEM_ACCOUNT = "system";

    public static final String SPRING_PROFILE_FAST = "fast";
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";
    // Spring profile used when deploying to Heroku
    public static final String SPRING_PROFILE_HEROKU = "heroku";

    //不是final的,防止编译优化问题
    public static int MAX_EXCEL_ROW_COUNT = 65000;

    private Constants() {
    }

    private static Map<String, String> cache = Maps.newLinkedHashMap();

    public static void update(String key, String value) {
        cache.put(key, value);
    }

    private static Map<String, List<Announcement>> announcementMap = Maps.newTreeMap();

    public static void update(String key, List<Announcement> value) {
        announcementMap.put(key, value);
    }

    /**
     * 具体的key见configuration表的value数据
     *
     * @param key
     * @return value of key
     */
    public static String get(String key) {
        return cache.get(key);
    }

    public static List<Announcement> getAnnouncementList(String key) {
        return announcementMap.get(key);
    }

    public static Integer getInt(String key) {
        String value = get(key);
        Integer ret = null;
        try {
            ret = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            ret = null;
        }
        return ret;
    }

    public static Integer getInt(String key, int defaultValue) {
        String value = get(key);
        Integer ret = null;
        try {
            ret = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            ret = defaultValue;
        }
        return ret;
    }

    public static BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        String value = get(key);
        BigDecimal ret = null;
        try {
            ret = new BigDecimal(value);
        } catch (NumberFormatException e) {
            ret = defaultValue;
        }
        return ret;
    }


    /**
     * 根据不同的操作系统返回不同的路径,规则是:
     * Windows|Mac|Linux
     *
     * @param path
     * @return 操作系统对应的路径
     */
    public static String getDataPath(String path) {
        String[] array = path.split("\\|");
        String userDir = System.getProperty("user.home");
        if (OSInfoUtils.isWindows()) {
            return array[0];
        } else if (OSInfoUtils.isMacOS()) {
            if (array[1].startsWith("~")) {
                return userDir + array[1].substring(1);
            }
            return array[1];
        } else if (OSInfoUtils.isLinux()) {
            if (array[2].startsWith("~")) {
                return userDir + array[2].substring(1);
            }
            return array[2];
        }
        return null;
    }


    public enum AttachedType {
        customer("客户"), image("图片");

        private String type;

        AttachedType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }




    /**
     * 模块
     */
    public enum ModuleType {

        user("用户管理"),

        member("客户管理"),

        stat("数据统计"),

        announcement("系统公告"),

        conf("系统配置"),

    	wechatCustomer("微信用户");

        private String type;

        ModuleType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public enum ItemType{

        b("购买"),

        g("增送");

        private String type;

        ItemType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }


    /**
     * 操作
     */
    public enum ActionType {

        c("创建"),

        u("更新"),

        d("删除"),

        bc("批量创建"),

        bu("批量更新"),

        bd("批量删除"),

        bi("批量导入"),

        exp("导出"),

        imp("导入"),

        rec("充值"),

        consumer("消费"),

        stat("统计"),

        login("登录"),

        refund("退款"),

        pwd("修改密码"),

        reset("重置");

        private String type;

        ActionType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

}
