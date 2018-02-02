package custom.tool.annotation;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * Created by monsoon on 15/11/5.
 */
public class Main {

    private static final String PROJECT = "poprice.wechat"; //项目包
    public static final String GEN_CLASS_NAME = PROJECT +".domain.CustomerItemRecord"; //Domain位置
    public static final String GEN_OUTPUT_DIR = "target/gen";//输出的文件夹
    public static final String GEN_BASE_DIR = "";//
    public static final boolean GEN_LIST_PAGED = true; //列表页是分页还是非分页

    public static void main(String[] args) {
        //ALL是代码+页面 还有其他ENUM
        AnalyticalDomain worker = new AnalyticalDomain(PROJECT,GEN_CLASS_NAME,GEN_OUTPUT_DIR,GEN_BASE_DIR, GEN_LIST_PAGED, AnalyticalDomain.AnalyticalType.ALL);
        worker.build();

        /*
        //测试修改md5加密后的密码的问题
        Md5PasswordEncoder md5 = new Md5PasswordEncoder();
        String result = md5.encodePassword("mstzero@admin.2", null);
        result = md5.encodePassword("k_wufang123", null);
        System.out.println(result);
        //*/

    }
}
