package com.yeremeyev.apiservant.teststeps.iterable.datareader;

public enum DataSourceType {
    MANUAL_GRID("manual_grid");

    private String type;

    private DataSourceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static DataSourceType fromType(String typeValue, DataSourceType defaultValue) {
        for (DataSourceType dataSourceType : values()) {
            if (dataSourceType.getType().equals(typeValue)) {
                return dataSourceType;
            }
        }
        return defaultValue;
    }
}
