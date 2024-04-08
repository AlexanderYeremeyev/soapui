package com.yeremeyev.apiservant.teststeps.readtextfile.ui;

import com.eviware.soapui.impl.EmptyPanelBuilder;
import com.eviware.soapui.ui.desktop.DesktopPanel;
import com.yeremeyev.apiservant.teststeps.readtextfile.ReadTextFileTestStep;

public class ReadTextFileStepPanelBuilder extends EmptyPanelBuilder<ReadTextFileTestStep> {

    public ReadTextFileStepPanelBuilder() {
    }

    @Override
    public DesktopPanel buildDesktopPanel(ReadTextFileTestStep testStep) {
        return new ReadTextFileStepDesktopPanel(testStep);
    }

    public boolean hasDesktopPanel() {
        return true;
    }
}