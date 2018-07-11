package com.template.common.configuration.properties;

import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

public class Servers {

    private Map<String, String> url = new HashMap<>();

    public void setUrl(Map<String, String> url) {
        this.url = url;
    }

    public Map<String, String> getUrl() {
        return url;
    }

    public String getUrl(String key) {
        return MapUtils.getString(url, key);
    }
}
