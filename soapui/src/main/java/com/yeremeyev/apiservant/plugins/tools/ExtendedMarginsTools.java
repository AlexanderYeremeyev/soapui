package com.yeremeyev.apiservant.plugins.tools;

import com.yeremeyev.apiservant.plugins.interfaces.margins.WindowAlignment;
import com.yeremeyev.apiservant.plugins.interfaces.margins.WindowMargin;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.List;

public class ExtendedMarginsTools {

    private static String getMarginPosition(WindowMargin margin) {
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
    public static JPanel createExtendedMarginsComponent(JPanel basePanel, List<WindowMargin> extendersList) {
        if (extendersList == null || extendersList.isEmpty()) {
            return basePanel;
        }
        JPanel extendedPanel = new JPanel(new BorderLayout());
        for (WindowMargin margin : extendersList) {
            JComponent extendedMargin = margin.getWindow();
            String marginPosition = getMarginPosition(margin);
            extendedPanel.add(extendedMargin, marginPosition);
        }
        extendedPanel.add(basePanel, BorderLayout.CENTER);
        return extendedPanel;
    }
}
