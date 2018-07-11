package com.template.common.restfull.annotation;

public enum RestServer {
    PROCESS_SERVER("process-server"),
    BOARD_SERVER("board-server");

    private String key;

    public String getKey() {
        return key;
    }

    RestServer(String key) {
        this.key = key;
    }
}
