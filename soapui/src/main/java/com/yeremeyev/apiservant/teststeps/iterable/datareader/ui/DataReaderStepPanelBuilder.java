package com.yeremeyev.apiservant.teststeps.iterable.datareader.ui;

import com.eviware.soapui.impl.EmptyPanelBuilder;
import com.eviware.soapui.ui.desktop.DesktopPanel;
import com.yeremeyev.apiservant.teststeps.iterable.datareader.DataReaderTestStep;

public class DataReaderStepPanelBuilder extends EmptyPanelBuilder<DataReaderTestStep> {

    public DataReaderStepPanelBuilder() {
    }

    @Override
    public DesktopPanel buildDesktopPanel(DataReaderTestStep testStep) {
        return new DataReaderStepDesktopPanel(testStep);
    }

    public boolean hasDesktopPanel() {
        return true;
    }
}