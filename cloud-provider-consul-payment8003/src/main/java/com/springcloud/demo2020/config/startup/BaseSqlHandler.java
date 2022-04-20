package com.springcloud.demo2020.config.startup;

import com.springcloud.demo2020.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DJH
 */
public abstract class BaseSqlHandler {

    private Statement statement;

    private Logger logger = LoggerFactory.getLogger(BaseSqlHandler.class);
    /**
     *  存放表结构更改的sql文件。此类更改不受事务管控
     */
    private List<File> dll = new ArrayList<>();

    /**
     *  数据初始化的sql文件。此类更改受事务管控
     */
    private List<File> dataInitSql = new ArrayList<>();

    /**
     * 收集文件
     * @param file 文件
     */
    public void collect(File file) {
        if (file == null) {
            logger.error("添加文件为空！");
            throw new RuntimeException("添加文件为空！");
        }

        String name = file.getName();
        if (!name.endsWith(".sql")) {
            return;
        }

        if (name.endsWith("dll.sql")) {
            dll.add(file);
        } else {
            dataInitSql.add(file);
        }
    }

    public void collect(File... files) {
        for (File file: files) {
            collect(file);
        }
    }

    /**
     * 执行所有sql文件
     * @param statement Statement
     * @throws IOException IOException
     */
    public void run(Statement statement) throws Exception {
        if (statement  == null) {
            logger.error("没有获取数据库连接！");
            throw new RuntimeException("没有获取数据库连接！");
        }

        this.statement = statement;

        // 先执行dll再执行一般的sql
        executeFiles(dll, true, false);
        executeFiles(dataInitSql, false, true);
    }


    /**
     * 处理dll的sql
     * @param files 文件列表
     * @param isLineRun 是否逐条执行
     * @param isThrow 是否抛出sql异常
     */
    private void executeFiles(List<File> files, boolean isLineRun, boolean isThrow) throws Exception {
        for (File file: files) {
            String sql = FileUtil.readFile(file);
            sql = wrapSql(sql);

            logger.info("执行sql文件：{}", file.getName());
            if (!isLineRun) {
                execute(sql, isThrow);
                continue;
            }

            String[] sqlArr = sql.split(";");
            for (String sqlLine: sqlArr) {
                execute(sqlLine, isThrow);
            }
        }
    }

    private void execute(String sql, boolean isThrow) throws SQLException {
        if (sql == null) {
            return;
        }
        sql = sql.trim();
        if ("\n".equalsIgnoreCase(sql) || "".equalsIgnoreCase(sql)) {
            return ;
        }

        try {
            statement.execute(sql);
            logger.info("\n{}", sql);
        } catch (SQLException e) {
            logger.error("sql执行失败：\n{}", sql);
            if (isThrow) {
                throw  e;
            }
        }
    }

    /**
     * 读取文件中的sql之后的处理
     * @param sql   读取出来的sql内容
     * @return  处理后的sql内容
     */
    protected abstract String wrapSql(String sql);
}
