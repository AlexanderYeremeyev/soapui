package com.eviware.soapui.impl.rest.panels.resource;

import com.eviware.soapui.impl.rest.support.RestParamsPropertyHolder;

/**
 * @author alex-tula
 */
public enum RestParamsTableMode {
    MINIMAL(new String[]{RestParamsTableModelData.ENABLE, RestParamsTableModelData.NAME, RestParamsTableModelData.VALUE},
            new Class[]{Boolean.class, String.class, String.class}),
    MEDIUM(new String[]{RestParamsTableModelData.ENABLE, RestParamsTableModelData.NAME, RestParamsTableModelData.VALUE, RestParamsTableModelData.STYLE},
            new Class[]{Boolean.class, String.class, String.class, RestParamsPropertyHolder.ParameterStyle.class}),
    FULL(RestParamsTableModelData.COLUMN_NAMES, RestParamsTableModelData.COLUMN_TYPES);

    private final String[] columnNames;

    private final Class[] columnTypes;

    private RestParamsTableMode(String[] columnNames, Class[] columnTypes) {
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public Class[] getColumnTypes() {
        return columnTypes;
    }
}
