package com.eviware.soapui.impl.rest.panels.resource;

import com.eviware.soapui.impl.rest.actions.support.NewRestResourceActionBase;
import com.eviware.soapui.impl.rest.support.RestParamsPropertyHolder;

/**
 * @author alex-tula
 */
public class RestParamsTableModelData {

    public static final String NAME = "Name";
    public static final String VALUE = "Value";
    public static final String STYLE = "Style";

    public static String[] COLUMN_NAMES = new String[]{NAME, "Default value", STYLE, "Level"};
    public static Class[] COLUMN_TYPES = new Class[]{String.class, String.class, RestParamsPropertyHolder.ParameterStyle.class, NewRestResourceActionBase.ParamLocation.class};
}
