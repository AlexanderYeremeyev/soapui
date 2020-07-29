package com.eviware.soapui.ui;

import com.eviware.soapui.ui.navigator.NavigatorNodesExpandStateProvider;

public class UIManager {
    private static NavigatorNodesExpandStateProvider navigatorNodesExpandStateProvider = null;

    public static NavigatorNodesExpandStateProvider getNavigatorNodesExpandStateProvider() {
        return navigatorNodesExpandStateProvider;
    }

    public static void setNavigatorNodesExpandStateProvider(NavigatorNodesExpandStateProvider navigatorNodesExpandStateProvider) {
        UIManager.navigatorNodesExpandStateProvider = navigatorNodesExpandStateProvider;
    }
}