package com.template.common.datasource.properties;

import java.util.List;

public class Mybatis {
    private String typeAliasesPackage;
    private String configLocation;
    private List<String> mapperLocations;
    private boolean failFast;

    public String getTypeAliasesPackage() {
        return typeAliasesPackage;
    }

    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    public String getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    public List<String> getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(List<String> mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public boolean isFailFast() {
        return failFast;
    }

    public void setFailFast(boolean failFast) {
        this.failFast = failFast;
    }
}