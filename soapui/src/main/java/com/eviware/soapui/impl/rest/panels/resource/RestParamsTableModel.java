/*
 * SoapUI, Copyright (C) 2004-2022 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon as they will be approved by the European Commission - subsequent 
 * versions of the EUPL (the "Licence"); 
 * You may not use this work except in compliance with the Licence. 
 * You may obtain a copy of the Licence at: 
 * 
 * http://ec.europa.eu/idabc/eupl 
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is 
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the Licence for the specific language governing permissions and limitations 
 * under the Licence. 
 */

package com.eviware.soapui.impl.rest.panels.resource;

import com.eviware.soapui.impl.rest.RestRequest;
import com.eviware.soapui.impl.rest.support.RestParamProperty;
import com.eviware.soapui.impl.rest.support.RestParamsPropertyHolder;
import com.eviware.soapui.impl.rest.support.RestParamsPropertyHolder.ParameterStyle;
import com.eviware.soapui.impl.wsdl.panels.teststeps.support.DirectAccessPropertyHolderTableModel;

import static com.eviware.soapui.impl.rest.actions.support.NewRestResourceActionBase.ParamLocation;

public class RestParamsTableModel extends DirectAccessPropertyHolderTableModel<RestParamsPropertyHolder> {

    public static final int ENABLE_COLUMN_INDEX = 0;
    public static final int NAME_COLUMN_INDEX = 1;
    public static final int VALUE_COLUMN_INDEX = 2;
    public static final int STYLE_COLUMN_INDEX = 3;
    public static final int LOCATION_COLUMN_INDEX = 4;

    private RestParamsTableMode mode;

    public RestParamsTableModel(RestParamsPropertyHolder params, RestParamsTableMode mode) {
        super(params);
        this.mode = mode;

        if (params.getModelItem() != null) {
            params.getModelItem().addPropertyChangeListener(this);
        }
    }

    public RestParamsTableModel(RestParamsPropertyHolder params) {
        this(params, RestParamsTableMode.FULL);
    }

    public boolean isInMinimalMode() {
        return mode == RestParamsTableMode.MINIMAL;
    }

    @Override
    public int getColumnCount() {
        return mode.getColumnTypes().length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (isColumnIndexOutOfBound(columnIndex)) {
            return null;
        }
        return mode.getColumnNames()[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (isColumnIndexOutOfBound(columnIndex)) {
            return null;
        }
        return mode.getColumnTypes()[columnIndex];
    }

    private boolean isColumnIndexOutOfBound(int columnIndex) {
        return columnIndex < 0 || columnIndex >= mode.getColumnTypes().length;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (mode == RestParamsTableMode.REST_TEST_STEP) {
            return columnIndex == 2;
        }
        return true;
    }

    public ParamLocation getParamLocationAt(int rowIndex) {
        return getParameterAt(rowIndex).getParamLocation();
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RestParamProperty prop = getParameterAt(rowIndex);

        switch (columnIndex) {
            case ENABLE_COLUMN_INDEX:
                return prop.isEnable();
            case NAME_COLUMN_INDEX:
                return prop.getName();
            case VALUE_COLUMN_INDEX:
                return prop.getValue();
            case STYLE_COLUMN_INDEX:
                return mode == RestParamsTableMode.MINIMAL ? null : prop.getStyle();
            case LOCATION_COLUMN_INDEX:
                return (mode != RestParamsTableMode.FULL && mode != RestParamsTableMode.REST_TEST_STEP) ? null : prop.getParamLocation();
        }

        return null;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (mode == RestParamsTableMode.REST_TEST_STEP && columnIndex != 2) {
            return;
        }
        RestParamProperty prop = getParameterAt(rowIndex);

        switch (columnIndex) {
            case ENABLE_COLUMN_INDEX: {
                prop.setEnable(Boolean.valueOf(value.toString()));
            }
            return;
            case NAME_COLUMN_INDEX:
                if (propertyExists(value, prop)) {
                    return;
                }

                params.renameProperty(prop.getName(), value.toString());
                return;
            case VALUE_COLUMN_INDEX:
                //if( !prop.getParamLocation().equals( ParamLocation.REQUEST ) )
                //{
                prop.setDefaultValue(value.toString());
                //}
                prop.setValue(value.toString());
                return;
            case STYLE_COLUMN_INDEX:
                if (mode != RestParamsTableMode.MINIMAL) {
                    prop.setStyle((ParameterStyle) value);
                }
                return;
            case LOCATION_COLUMN_INDEX:
                if (mode == RestParamsTableMode.FULL) {
                    if (params.getModelItem() != null && params.getModelItem() instanceof RestRequest) {
                        this.isLastChangeParameterLevelChange = true;
                    }
                    params.setParameterLocation(prop, (ParamLocation) value);
                }
        }
    }

    public RestParamProperty getParameterAt(int selectedRow) {
        return (RestParamProperty) super.getPropertyAtRow(selectedRow);
    }

    public ParamLocation[] getParameterLevels() {
        return ParamLocation.values();
    }

    public void setParams(RestParamsPropertyHolder params) {
        this.params.removeTestPropertyListener(testPropertyListener);
        this.params = params;
        this.params.addTestPropertyListener(testPropertyListener);

        fireTableDataChanged();
    }

    public void removeProperty(String propertyName) {
        params.remove(propertyName);
    }


}
