package com.eviware.soapui.plugins.storage;

import com.eviware.soapui.plugins.interfaces.margin.ExtendedWindowMargin;
import com.eviware.soapui.plugins.interfaces.margin.WindowType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalWindowMarginsStorage {
    private static Map<WindowType, List<ExtendedWindowMargin>> windowsMap = new HashMap<>();

    private static List<ExtendedWindowMargin> getWindowsListInternal(WindowType windowType) {
        List<ExtendedWindowMargin> result = null;
        if (windowsMap.containsKey(windowType)) {
            result = windowsMap.get(windowType);
        } else {
            result = new ArrayList<>();
            windowsMap.put(windowType, result);
        }
        return result;
    }

    public static synchronized void addMargin(ExtendedWindowMargin extendedWindowMargin) {
        if (extendedWindowMargin == null) {
            return;
        }
        List<ExtendedWindowMargin> windowsList = getWindowsListInternal(extendedWindowMargin.getWindowType());
        windowsList.add(extendedWindowMargin);
    }

    public static synchronized List<ExtendedWindowMargin> getMarginsList(WindowType windowType) {
        List<ExtendedWindowMargin> windowsList = getWindowsListInternal(windowType);
        List<ExtendedWindowMargin> result = new ArrayList<>();
        result.addAll(windowsList);
        return result;
    }
}