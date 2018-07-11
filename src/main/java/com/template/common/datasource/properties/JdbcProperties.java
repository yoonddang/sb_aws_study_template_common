package com.template.common.datasource.properties;

import java.util.Map;

public class JdbcProperties {

    private Map<String, Integer> environment;
    private Map<String, Object> atomikos;
    private Map<String, String> jta;

    public Map<String, Integer> getEnvironment() {
        return environment;
    }

    public void setEnvironment(Map<String, Integer> environment) {
        this.environment = environment;
    }

    public Map<String, Object> getAtomikos() {
        return atomikos;
    }

    public void setAtomikos(Map<String, Object> atomikos) {
        this.atomikos = atomikos;
    }

    public Map<String, String> getJta() {
        return jta;
    }

    public void setJta(Map<String, String> jta) {
        this.jta = jta;
    }
}
