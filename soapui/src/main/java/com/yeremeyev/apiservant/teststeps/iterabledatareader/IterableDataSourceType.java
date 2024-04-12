package com.yeremeyev.apiservant.teststeps.iterabledatareader;

public enum IterableDataSourceType {
    MANUAL_GRID("manual_grid")
    ;

    private String type;

    private IterableDataSourceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
