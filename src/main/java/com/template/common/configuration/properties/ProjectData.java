package com.template.common.configuration.properties;

public class ProjectData {
    private String name;
    private Servers servers;
    private String bannedWord;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Servers getServers() {
        return servers;
    }

    public void setServers(Servers servers) {
        this.servers = servers;
    }

    public String getBannedWord() {
        return bannedWord;
    }

    public void setBannedWord(String bannedWord) {
        this.bannedWord = bannedWord;
    }
}
