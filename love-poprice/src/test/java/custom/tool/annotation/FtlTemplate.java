package custom.tool.annotation;

import freemarker.template.*;

import java.io.*;
import java.util.Map;

/**
 * 获取模板后生成文件
 */
public class FtlTemplate {

    private String template;

    public FtlTemplate(String template) {
        this.template = template;
    }

    public Template getTemplate() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        Template t = null;
        try {
            cfg.setDirectoryForTemplateLoading(new File("src/test/resources/ftl"));
            t = cfg.getTemplate(template);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }
}
