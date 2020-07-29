package com.eviware.soapui.plugins.tools;

import com.eviware.soapui.plugins.interfaces.margin.ExtendedWindowMargin;
import com.eviware.soapui.plugins.interfaces.margin.WindowAlignment;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.List;

public class ExtendedMarginsTools {

    private static String getMarginPosition(ExtendedWindowMargin margin) {
        WindowAlignment windowAlignment = margin.getWindowAlignment();
        String marginPosition = BorderLayout.EAST;
        switch (windowAlignment) {
            case TOP:
                marginPosition = BorderLayout.NORTH;
                break;
            case RIGHT:
                marginPosition = BorderLayout.EAST;
                break;
            case BOTTOM:
                marginPosition = BorderLayout.SOUTH;
                break;
            case LEFT:
                marginPosition = BorderLayout.WEST;
                break;
        }
        return marginPosition;
    }

    /**
     * @param basePanel         target panel which we want to extend
     * @param extendersList extended margins list
     * @return composition of basePanel and extendersList
     */
    public static JPanel createExtendedMarginsComponent(JPanel basePanel, List<ExtendedWindowMargin> extendersList) {
        if (extendersList == null || extendersList.isEmpty()) {
            return basePanel;
        }
        JPanel extendedPanel = new JPanel(new BorderLayout());
        for (ExtendedWindowMargin margin : extendersList) {
            JComponent extendedMargin = margin.getWindow();
            String marginPosition = getMarginPosition(margin);
            extendedPanel.add(extendedMargin, marginPosition);
        }
        extendedPanel.add(basePanel, BorderLayout.CENTER);
        return extendedPanel;
    }
}
