package com.yeremeyev.apiservant.plugins.interfaces.margins;

import javax.swing.JComponent;

/**
 * base interface for extended windows.
 * for example describe target window type, position of extended window and contain customer window.
 */
public interface WindowMargin {

    WindowType getWindowType();

    WindowAlignment getWindowAlignment();

    JComponent getWindow();
}