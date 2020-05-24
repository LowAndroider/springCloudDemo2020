package com.springcloud.demo2020.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    /**
     * 读取文件内容
     * @param file  文件
     * @return  sql
     * @throws IOException IOException
     */
    public static String readFile(File file) throws Exception {
        if (file == null) {
            throw new Exception("所选文件为null");
        }

        StringBuilder script = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
        BufferedReader lineReader = new BufferedReader(inputStreamReader);

        String line;
        while ((line = lineReader.readLine()) != null) {
            script.append(line).append("\n");
        }

        return script.toString();
    }

}
