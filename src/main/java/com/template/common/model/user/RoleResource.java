package com.template.common.model.user;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

public class RoleResource{
    private Map<Integer, String> users = new HashMap<>();

    public Map<Integer, String> getUsers(){
        return this.users;
    }
}