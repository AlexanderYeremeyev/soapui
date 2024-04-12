package com.yeremeyev.apiservant.teststeps.iterabledatareader.ui;

import com.eviware.soapui.impl.EmptyPanelBuilder;
import com.eviware.soapui.ui.desktop.DesktopPanel;
import com.yeremeyev.apiservant.teststeps.readtextfile.ReadTextFileTestStep;

public class IterableDataReaderStepPanelFactory extends EmptyPanelBuilder<ReadTextFileTestStep> {

    public IterableDataReaderStepPanelFactory() {
    }

    @Override
    public DesktopPanel buildDesktopPanel(ReadTextFileTestStep testStep) {
        return new IterableDataReaderStepDesktopPanel(testStep);
    }

    public boolean hasDesktopPanel() {
        return true;
    }
}