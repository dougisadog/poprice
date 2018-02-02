package custom.tool.annotation;

import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Id;
import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by monsoon on 15/11/5.
 * 用于分析domain中定义的属性
 */
public class AnalyticalDomain {
    private String projectName;
    private String genClassName;
    private String genOutputDir;
    private String genBaseDir;
    private boolean genListPaged;
    private AnalyticalType type;

    /**
     * @param projectName  项目名称
     * @param genClassName 要输出生成的domain
     * @param genOutputDir 输出文件夹
     * @param genBaseDir   子文件夹
     * @param genListPaged 列表页是否分页
     * @param type         输出类型
     */
    public AnalyticalDomain(String projectName, String genClassName, String genOutputDir, String genBaseDir, boolean genListPaged, AnalyticalType type) {
        this.projectName = projectName;
        this.genClassName = genClassName;
        this.genOutputDir = genOutputDir;
        this.genBaseDir = genBaseDir;
        this.genListPaged = genListPaged;
        this.type = type;
    }

    /****************************************************************************************************/
    private Class<?> clazz;

    public void build() {
        System.out.println("生成开始 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        try {
            this.clazz = Class.forName(genClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        switch (type) {
            case ALL:
                buildRepository();
                buildService();
                buildController();
                buildList();
                buildCreate();
                buildEdit();
                buildForm();
                buildShow();
                break;
            case CODE:
                buildRepository();
                buildService();
                buildController();
                break;
            case VIEW:
                buildList();
                buildCreate();
                buildEdit();
                buildForm();
                buildShow();
                break;
        }
        System.out.println("生成结束 =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
    }

    private void buildRepository() {
        //组织数据
        Map<String, Object> map = new HashMap<>();
        map.put("packageName", this.clazz.getPackage().toString().replace("domain", "repository")); //repository包名
        map.put("classPackageName", this.clazz.getName()); //类的包名
        map.put("className", this.clazz.getSimpleName());//类名

        FtlTemplate ftlTemplate = new FtlTemplate("repository.ftl");
        Template temp = ftlTemplate.getTemplate();

        try {
            //要输出文件夹
            String OutputDir = genOutputDir + "/" + genBaseDir + "/";
            File tempDir = new File(OutputDir);
            tempDir.mkdirs();
            //输出的文件名称
            OutputStream out = new FileOutputStream(OutputDir + this.clazz.getSimpleName() + "Repository.java");
            Writer osr = new OutputStreamWriter(out);
            temp.process(map, osr);//生成
            out.flush();
            System.out.println("生成文件" + this.clazz.getSimpleName() + "Repository.java 成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildService() {
        //组织数据
        Map<String, Object> map = new HashMap<>();
        map.put("packageName", this.clazz.getPackage().toString().replace("domain", "service")); //service包名
        map.put("classPackageName", this.clazz.getName()); //类的包名
        map.put("className", this.clazz.getSimpleName());//类名
        map.put("repositoryClassPackageName", this.clazz.getName().toString().replace("domain", "repository").replace(this.clazz.getSimpleName(), this.clazz.getSimpleName() + "Repository")); //repository包名
        map.put("repositoryClassName", this.clazz.getSimpleName() + "Repository");//repository类名
        map.put("repositoryClassSimpleName", this.clazz.getSimpleName().substring(0, 1).toLowerCase() + this.clazz.getSimpleName().substring(1) + "Repository");//小写repository类名

        FtlTemplate ftlTemplate = new FtlTemplate("service.ftl");
        Template temp = ftlTemplate.getTemplate();
        try {
            //要输出文件夹
            String OutputDir = genOutputDir + "/" + genBaseDir + "/";
            File tempDir = new File(OutputDir);
            tempDir.mkdirs();
            //输出的文件名称
            OutputStream out = new FileOutputStream(OutputDir + this.clazz.getSimpleName() + "Service.java");
            Writer osr = new OutputStreamWriter(out);
            temp.process(map, osr);//生成
            out.flush();
            System.out.println("生成文件" + this.clazz.getSimpleName() + "Service.java 成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildController() {
        //组织数据
        Map<String, Object> map = new HashMap<>();
        map.put("projectName", projectName); //项目名称
        map.put("packageName", projectName + ".web.mvc." + genBaseDir); //controller包名
        map.put("classPackageName", this.clazz.getName()); //类的包名
        map.put("className", this.clazz.getSimpleName());//类名
        map.put("repositoryClassPackageName", this.clazz.getName().toString().replace("domain", "repository").replace(this.clazz.getSimpleName(), this.clazz.getSimpleName() + "Repository")); //repository包名
        map.put("repositoryClassName", this.clazz.getSimpleName() + "Repository");//repository类名
        map.put("repositoryClassSimpleName", this.clazz.getSimpleName().substring(0, 1).toLowerCase() + this.clazz.getSimpleName().substring(1) + "Repository");//小写repository类名
        if (StringUtils.isBlank(genBaseDir)) {
            map.put("mapping", "/" + this.clazz.getSimpleName().substring(0, 1).toLowerCase() + this.clazz.getSimpleName().substring(1));//mapping
        } else {
            map.put("mapping", "/" + genBaseDir + "/" + this.clazz.getSimpleName().substring(0, 1).toLowerCase() + this.clazz.getSimpleName().substring(1));//mapping
        }

        FtlTemplate ftlTemplate;
        //分页
        if (genListPaged) {
            ftlTemplate = new FtlTemplate("pageController.ftl");
        } else {
            ftlTemplate = new FtlTemplate("listController.ftl");
        }
        Template temp = ftlTemplate.getTemplate();
        try {
            //要输出文件夹
            String OutputDir = genOutputDir + "/" + genBaseDir + "/";
            File tempDir = new File(OutputDir);
            tempDir.mkdirs();
            //输出的文件名称
            OutputStream out = new FileOutputStream(OutputDir + this.clazz.getSimpleName() + "Controller.java");
            Writer osr = new OutputStreamWriter(out);
            temp.process(map, osr);//生成
            out.flush();
            System.out.println("生成文件" + this.clazz.getSimpleName() + "Controller.java 成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void buildList() {
        Map<String, String> parameterMap = new TreeMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                continue;
            }
            parameterMap.put(field.getName(), field.getName());
          //  if (field.isAnnotationPresent(RenderParameter.class)) {
             //   RenderParameter parameter = (RenderParameter) field.getAnnotation(RenderParameter.class);
                //if (parameter.onPage()) {
//                    parameterMap.put(parameter.label(), field.getName());
//                }
//            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("parameterMap", parameterMap);
        if (StringUtils.isBlank(genBaseDir)) {
            map.put("mapping", "/" + this.clazz.getSimpleName().substring(0, 1).toLowerCase() + this.clazz.getSimpleName().substring(1));//mapping
        } else {
            map.put("mapping", "/" + genBaseDir + "/" + this.clazz.getSimpleName().substring(0, 1).toLowerCase() + this.clazz.getSimpleName().substring(1));//mapping
        }

        FtlTemplate ftlTemplate;
        //分页
        if (genListPaged) {
            ftlTemplate = new FtlTemplate("page.ftl");
        } else {
            ftlTemplate = new FtlTemplate("list.ftl");
        }
        Template temp = ftlTemplate.getTemplate();
        try {
            //要输出文件夹
            String OutputDir = genOutputDir + "/" + genBaseDir + "/";
            File tempDir = new File(OutputDir);
            tempDir.mkdirs();
            //输出的文件名称
            OutputStream out = new FileOutputStream(OutputDir + "list.html");
            Writer osr = new OutputStreamWriter(out);
            temp.process(map, osr);//生成
            out.flush();
            System.out.println("生成文件" + "list.html 成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildCreate() {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(genBaseDir)) {
            map.put("mapping", "/" + this.clazz.getSimpleName().substring(0, 1).toLowerCase() + this.clazz.getSimpleName().substring(1));//mapping
        } else {
            map.put("mapping", "/" + genBaseDir + "/" + this.clazz.getSimpleName().substring(0, 1).toLowerCase() + this.clazz.getSimpleName().substring(1));//mapping
        }

        FtlTemplate ftlTemplate = new FtlTemplate("create.ftl");
        Template temp = ftlTemplate.getTemplate();
        try {
            //要输出文件夹
            String OutputDir = genOutputDir + "/" + genBaseDir + "/";
            File tempDir = new File(OutputDir);
            tempDir.mkdirs();
            //输出的文件名称
            OutputStream out = new FileOutputStream(OutputDir + "create.html");
            Writer osr = new OutputStreamWriter(out);
            temp.process(map, osr);//生成
            out.flush();
            System.out.println("生成文件" + "create.html 成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildEdit() {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(genBaseDir)) {
            map.put("mapping", "/" + this.clazz.getSimpleName().substring(0, 1).toLowerCase() + this.clazz.getSimpleName().substring(1));//mapping
        } else {
            map.put("mapping", "/" + genBaseDir + "/" + this.clazz.getSimpleName().substring(0, 1).toLowerCase() + this.clazz.getSimpleName().substring(1));//mapping
        }

        FtlTemplate ftlTemplate = new FtlTemplate("edit.ftl");
        Template temp = ftlTemplate.getTemplate();
        try {
            //要输出文件夹
            String OutputDir = genOutputDir + "/" + genBaseDir + "/";
            File tempDir = new File(OutputDir);
            tempDir.mkdirs();
            //输出的文件名称
            OutputStream out = new FileOutputStream(OutputDir + "edit.html");
            Writer osr = new OutputStreamWriter(out);
            temp.process(map, osr);//生成
            out.flush();
            System.out.println("生成文件" + "edit.html 成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildForm() {
        Map<String, String> parameterMap = new TreeMap<>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.getName().equals("id")) {
                continue;
            }

            //TODO Monsoon: 未来根据NotBlank和Size等可以进一步生成更详细的.
            //1.自然序的问题,需要parse源代码
            //2.日期自动生成弹出以及各式
            //3.进一步对校验等,包括js校验进行支持
            //4.i18n support
            //5.可定制的one-to-many或者many-to-many
            //6.字典弹出模块的自动生成,可配置
            //7.日历模块更换
            //8.一点想法:元数据声明绝对不是最好的方式,不该用元数据来辅助生成.

            if (field.isAnnotationPresent(RenderParameter.class)) {
                RenderParameter parameter = (RenderParameter) field.getAnnotation(RenderParameter.class);
                parameterMap.put(parameter.label(), field.getName());
            } else {
                parameterMap.put(field.getName(), field.getName());
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("parameterMap", parameterMap);

        FtlTemplate ftlTemplate = new FtlTemplate("form.ftl");
        Template temp = ftlTemplate.getTemplate();
        try {
            //要输出文件夹
            String OutputDir = genOutputDir + "/" + genBaseDir + "/";
            File tempDir = new File(OutputDir);
            tempDir.mkdirs();
            //输出的文件名称
            OutputStream out = new FileOutputStream(OutputDir + "form.html");
            Writer osr = new OutputStreamWriter(out);
            temp.process(map, osr);//生成
            out.flush();
            System.out.println("生成文件" + "form.html 成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildShow() {
        Map<String, String> parameterMap = new TreeMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(RenderParameter.class)) {
                RenderParameter parameter = (RenderParameter) field.getAnnotation(RenderParameter.class);
                parameterMap.put(parameter.label(), field.getName());
            } else {
                parameterMap.put(field.getName(), field.getName());
            }
        }


        Map<String, Object> map = new HashMap<>();
        map.put("parameterMap", parameterMap);
        if (StringUtils.isBlank(genBaseDir)) {
            map.put("mapping", "/" + this.clazz.getSimpleName().substring(0, 1).toLowerCase() + this.clazz.getSimpleName().substring(1));//mapping
        } else {
            map.put("mapping", "/" + genBaseDir + "/" + this.clazz.getSimpleName().substring(0, 1).toLowerCase() + this.clazz.getSimpleName().substring(1));//mapping
        }


        FtlTemplate ftlTemplate = new FtlTemplate("show.ftl");
        Template temp = ftlTemplate.getTemplate();
        try {
            //要输出文件夹
            String OutputDir = genOutputDir + "/" + genBaseDir + "/";
            File tempDir = new File(OutputDir);
            tempDir.mkdirs();
            //输出的文件名称
            OutputStream out = new FileOutputStream(OutputDir + "show.html");
            Writer osr = new OutputStreamWriter(out);
            temp.process(map, osr);//生成
            out.flush();
            System.out.println("生成文件" + "show.html 成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public enum AnalyticalType {
        ALL("所有"), CODE("代码"), VIEW("页面");
        private String type;

        AnalyticalType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }

    }

}
