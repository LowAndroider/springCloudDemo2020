package cn.dj;

import cn.dj.entity.DataSource;
import cn.dj.util.YamlUtils;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.BeetlTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {

    private final static Logger logger = LoggerFactory.getLogger(CodeGenerator.class);

    private final static String[] TABLE_ARRAY = {};

    private final static String MODULE_NAME = "code-generator";

    /**
     * 表名前缀
     */
    private final static String TABLE_PREFIX = "tb_pluss";

    private final static String AUTHOR = "Djh";

    private final static String PACKAGE_PREFIX = "cn.pluss.platform";

    /**
     * 模板路径
     */
    private final static String ENTITY = "templates/entity.java";
    //    private final static String MAPPER = "templates/mapper.java";
//    private final static String SERVICE = "templates/service.java";
//    private final static String SERVICE_IMPL = "templates/serviceImpl.java";
    private final static String CONTROLLER = "templates/controller.java";

    public static void main(String[] args) throws FileNotFoundException {
        YamlUtils yamlUtils = new YamlUtils();
        DataSource dataSource = yamlUtils.readDefaultYaml();

        AutoGenerator mpg = new AutoGenerator();
        // 选择 freemarker 引擎，默认 Velocity
        mpg.setTemplateEngine(new BeetlTemplateEngine());

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setAuthor(AUTHOR);
        gc.setFileOverride(true);// 覆盖同名文件
        gc.setOpen(false);// 不要打开生成后的目录
        String projectPath = System.getProperty("user.dir") + "/" + MODULE_NAME;// 获取项目根目录
        gc.setOutputDir(projectPath + "/src/main/java");// 设置代码生成的目录
        gc.setBaseResultMap(true);// mapper.xml中生成resultMap，默认false
        /* 自定义文件命名，注意 %s 会自动填充表实体属性！ */
        gc.setServiceName("%sService");// 默认生成的service接口会以I开头
        // gc.setActiveRecord(false);//ActiveRecord模式，默认false，实体类会继承Model<T>，可以使用实体类crud
        // gc.setSwagger2(true);//开启 swagger2 模式 默认false
        // gc.setIdType(IdType.AUTO);//主键生成策略 默认自动
        // gc.setEnableCache(false);// mapper.xml 二级缓存，默认false
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);//设置数据库类型
        dsc.setTypeConvert(new MySqlTypeConvert() {
            @Override
            public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                System.out.println("转换类型：" + fieldType);
                //tinyint转换成Boolean
                if (fieldType.toLowerCase().contains("tinyint")) {
                    return DbColumnType.BOOLEAN;
                }
                //将数据库中datetime转换成date
                if (fieldType.toLowerCase().contains("datetime")) {
                    return DbColumnType.DATE;
                }
                return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
            }

        });
        dsc.setUrl(dataSource.getUrl());
        dsc.setUsername(dataSource.getUsername());
        dsc.setPassword(dataSource.getPassword());
        dsc.setDriverName(dataSource.getDriverClassName());

        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        // 表前缀
        strategy.setTablePrefix(TABLE_PREFIX);
        // 表名生成策略，下划线转驼峰
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 列名下划线转驼峰
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 需要生成的表
        strategy.setInclude(TABLE_ARRAY);
        // 排除生成的表
        // strategy.setExclude(new String[]{"test"});
        // 实体类使用lombok
        strategy.setEntityLombokModel(true);
        // 自定填充策略注解
        List<TableFill> tableFieldList = new ArrayList<>();
        tableFieldList.add(new TableFill("createTime", FieldFill.INSERT));
        tableFieldList.add(new TableFill("updateTime", FieldFill.INSERT_UPDATE));
        strategy.setTableFillList(tableFieldList);
        // 生成 @RestController 控制器
        strategy.setRestControllerStyle(true);
        // 逻辑删除属性名称
        strategy.setLogicDeleteFieldName("is_delete");

        strategy.setEntityTableFieldAnnotationEnable(true);

        // 自定义实体父类
        // strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age"});
        // 自定义mapper父类
        strategy.setSuperMapperClass("com.baomidou.mybatisplus.core.mapper.BaseMapper");
        // 自定义service父类
        strategy.setSuperServiceClass("com.baomidou.mybatisplus.extension.service.IService");
        // 自定义service实现类父类
        strategy.setSuperServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl");
        // 自定义Controller父类
//        strategy.setSuperControllerClass("cn.pluss.platform.base.BaseModelController<${table.serviceName}, ${entity}>");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        strategy.setEntityBuilderModel(true);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(PACKAGE_PREFIX);
        // controller改成web
//        pc.setController("web");
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };
//        // 如果模板引擎是 freemarker
//        String templatePath = "/templates/mapper.xml.ftl";
        String templatePath = "/templates/mapper.xml.btl";
        List<FileOutConfig> focList = new ArrayList<>();
        // 调整 xml 生成目录，默认在mapper.xml包下
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper"
                        + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        TemplateConfig tc = new TemplateConfig();
        // 关闭默认 xml 生成，已经在src/main/resources/mapper下生成
        tc.setXml(null);
        tc.setEntity(ENTITY);
//        tc.setMapper(MAPPER);
//        tc.setService(SERVICE);
//        tc.setServiceImpl(SERVICE_IMPL);
        tc.setController(CONTROLLER);
        mpg.setTemplate(tc);

        logger.debug("执行生成");
        // 执行生成
        mpg.execute();
    }
}

