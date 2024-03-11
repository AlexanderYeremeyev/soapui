package com.yeremeyev.apiservant.teststeps.common.ui.content.views.interfaces;

import com.yeremeyev.java.core.interfaces.common.Releasable;

import javax.swing.JComponent;

public interface MessageView extends Releasable {

    public String getViewTitle();

    /**
     * for cases with lots of views. some of them may be heavy. such views must know where to be written
     *
     * @param active
     */
    public void setActive(boolean active);

    public boolean isActive();

    /**
     * @return java view component. usually itself.
     */
    public JComponent getComponent();
}
