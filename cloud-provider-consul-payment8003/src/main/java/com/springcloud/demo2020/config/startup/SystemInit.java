package com.springcloud.demo2020.config.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * @author Djh
 */
@Component
@DependsOn("springContextHolder")
public class SystemInit implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(SystemInit.class);

    private DataSource dataSource;

    @Resource
    private ResourceLoader resourceLoader;

    /**
     * 保存数据库连接信息
     */
    private Statement statement = null;
    private Connection connection = null;

    @Autowired
    public SystemInit(DataSource dataSource) {
        this.dataSource = dataSource;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
        } catch (SQLException e) {
            logger.error("无法获取数据库连接");
        }
    }

    private void init() throws Exception {
        String dataVersion;
        String lastVersion;

        try {
            dataVersion = InitHelper.getDataVersion(statement);
            List<File> initFiles;
            Map<String, Object> result = InitHelper.getInitDir(dataVersion, resourceLoader);
            initFiles = (List<File>) result.get("dir");
            lastVersion = (String) result.get("version");

            if (initFiles.isEmpty()) {
                return;
            }

            for (File file : initFiles) {
                if (!file.isDirectory()) {
                    continue;
                }

                File[] files = file.listFiles();
                if (files == null) {
                    return;
                }

                try {
                    BaseSqlHandler sqlEntity = new BaseSqlHandler() {
                        @Override
                        protected String wrapSql(String sql) {
                            return sql;
                        }
                    };
                    sqlEntity.collect(files);
                    sqlEntity.run(statement);
                } catch (SQLException e) {
                    logger.error("数据库初始化异常！");
                    throw e;
                } catch (IOException e) {
                    logger.error("读取初始化sql文件错误！");
                    throw e;
                }
            }

            updateVersion(lastVersion);
            commit();
        } catch (Exception e) {
            rollBack();
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private void updateVersion(String version) throws SQLException {
        statement = getStatement();
        statement.execute("UPDATE data_version SET version = '" + version + "'");
    }

    /**
     * 回滚所有的连接
     */
    private void rollBack() throws SQLException {
        try {
            connection.rollback();
            logger.error("初始化数据回滚！");
        } catch (SQLException e) {
            logger.error("初始化数据回滚失败！");
            throw e;
        }
    }

    private void commit() throws SQLException {
        try {
            connection.commit();
        } catch (SQLException e) {
            logger.error(" 初始化事务提交失败！");
            e.printStackTrace();
            throw e;
        }
    }

    private Statement getStatement() throws SQLException {

        if (connection == null) {
            throw new RuntimeException("没有获得数据库连接信息！");
        }

        if (statement == null) {
            statement = connection.createStatement();
        }

        return statement;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        init();
    }
}
