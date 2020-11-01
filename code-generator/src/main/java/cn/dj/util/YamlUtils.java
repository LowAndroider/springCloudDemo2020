package cn.dj.util;

import cn.dj.entity.DataSource;
import com.alibaba.fastjson.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;

/**
 * @author Djh
 */
public class YamlUtils {

    public DataSource readDefaultYaml() throws FileNotFoundException {
        Yaml yaml = new Yaml();

        URL url = this.getClass().getClassLoader().getResource("application.yml");
        if (url != null) {
            //获取test.yaml文件中的配置数据，然后转换为obj，
            //也可以将值转换为Map
            Map<String, Object> map = yaml.load(new FileInputStream(url.getFile()));
            System.out.println(map);
            return JSONObject.parseObject(JSONObject.toJSONString(map), DataSource.class);
        }

        return null;
    }

    public static void main(String[] args) throws FileNotFoundException {
        YamlUtils yamlUtils = new YamlUtils();
        yamlUtils.readDefaultYaml();
    }
}
