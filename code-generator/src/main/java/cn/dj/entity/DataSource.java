package cn.dj.entity;

import lombok.Data;

/**
 * @author mbp
 */
@Data
public class DataSource {

    private String url;

    private String username;

    private String password;

    private String driverClassName;
}
