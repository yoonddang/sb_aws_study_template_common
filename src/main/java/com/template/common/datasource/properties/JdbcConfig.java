package com.template.common.datasource.properties;

import com.template.common.datasource.DatasourceMeta;

import java.util.Map;

public class JdbcConfig {
    private JdbcProperties jdbc;
    private Mybatis mybatis;
    private String defaultDatasource;
    private String defaultDatasource2;
    private Map<String, DatasourceMeta> datasource;
    private Map<String, DatasourceMeta> datasource2;

    public String getDefaultDatasource() {
        return defaultDatasource;
    }

    public JdbcProperties getJdbc() {
        return jdbc;
    }

    public void setJdbc(JdbcProperties jdbc) {
        this.jdbc = jdbc;
    }

    public Mybatis getMybatis() {
        return mybatis;
    }

    public void setMybatis(Mybatis mybatis) {
        this.mybatis = mybatis;
    }

    public void setDefaultDatasource(String defaultDatasource) {
        this.defaultDatasource = defaultDatasource;
    }

    public String getDefaultDatasource2() {
        return defaultDatasource2;
    }

    public void setDefaultDatasource2(String defaultDatasource2) {
        this.defaultDatasource2 = defaultDatasource2;
    }

    public Map<String, DatasourceMeta> getDatasource() {
        return datasource;
    }

    public void setDatasource(Map<String, DatasourceMeta> datasource) {
        this.datasource = datasource;
    }

    public Map<String, DatasourceMeta> getDatasource2() {
        return datasource2;
    }

    public void setDatasource2(Map<String, DatasourceMeta> datasource2) {
        this.datasource2 = datasource2;
    }
}
