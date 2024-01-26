package com.yeremeyev.apiservant.ui.components.factories;

import com.yeremeyev.java.common.windows.swing.components.base.tabbedpane.TabbedPane;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

public class TabbedPaneFactory {

    public static JTabbedPane createDefaultTabbedPane(int tabPlacement) {
        TabbedPane tabbedPane = new TabbedPane(tabPlacement);
        return tabbedPane;
    }

    public static JTabbedPane createDefaultTabbedPane() {
        return createDefaultTabbedPane(SwingConstants.TOP);
    }
}
