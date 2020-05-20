package com.springcloud.demo2020.config.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * @author Djh
 */
public class InitHelper {

    private static final String DEFAULT_VERSION = "0";

    private static Logger logger = LoggerFactory.getLogger(InitHelper.class);

    private static final String VERSION_TABLE_CREATE_INIT = "" +
            "CREATE TABLE IF NOT EXISTS data_version (" +
            "version VARCHAR(32)" +
            ");";

    private static final String VERSION_INIT = "INSERT INTO data_version SELECT '" + DEFAULT_VERSION + "' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM data_version);";

    private static final String SELECT_VERSION = "SELECT version FROM data_version;";

    /**
     * 获取数据库数据的版本，默认为0
     * @return 数据库的版本
     */
    public static String getDataVersion(Statement statement) throws SQLException {
        try {
            String version = "none";
            statement.execute(VERSION_TABLE_CREATE_INIT);
            statement.execute(VERSION_INIT);
            ResultSet resultSet = statement.executeQuery(SELECT_VERSION);

            if (resultSet.next()) {
                version = resultSet.getString(1);
            }

            return StringUtils.isEmpty(version)? DEFAULT_VERSION: version;
        } catch (SQLException sqlException) {
            logger.error("无法获取数据库版本号！");
            sqlException.printStackTrace();
            throw sqlException;
        }
    }

    /**
     * 获取文件sql所有下级目录
     * @param currentVersion    当前版本
     * @return sql目录, 并返回最新的版本号
     * @throws FileNotFoundException 没有找到文件的异常
     */
    public static Map<String, Object> getInitDir(String currentVersion, ResourceLoader resourceLoader) throws IOException {
        Map<String, Object> result = new HashMap<>();
        String lastVersion = currentVersion;
        result.put("version", lastVersion);
        URL url = resourceLoader.getClass().getClassLoader().getResource("sql");
        File root = new File(url.getPath());
        File[] files = root.listFiles();
        List<File> dirList = new ArrayList<>();
        result.put("dir", dirList);
        if (files == null) {
            return result;
        }

        // 先根据名称排序
        Arrays.sort(files);

        for (File file : files) {
            if (!file.isDirectory()) {
                continue;
            }

            String fileName = file.getName();
            if (!fileName.startsWith("v")) {
                continue;
            }

            String versionCode = fileName.substring(1);
            if (versionCode.compareTo(currentVersion) > 0) {
                dirList.add(file);
            }

            if (versionCode.compareTo(lastVersion) > 0) {
                lastVersion = versionCode;
            }
        }

        result.put("version", lastVersion);
        return result;
    }
}
