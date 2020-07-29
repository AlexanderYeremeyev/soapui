package com.eviware.soapui.plugins.interfaces.margin;

import javax.swing.JComponent;

/**
 * base interface for extended windows.
 * for example describe target window type, position of extended window and contain customer window.
 */
public interface ExtendedWindowMargin {

    WindowType getWindowType();

    WindowAlignment getWindowAlignment();

    JComponent getWindow();
}
