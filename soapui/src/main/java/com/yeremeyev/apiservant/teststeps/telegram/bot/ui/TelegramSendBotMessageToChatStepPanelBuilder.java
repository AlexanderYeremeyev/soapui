package com.yeremeyev.apiservant.teststeps.telegram.bot.ui;

import com.eviware.soapui.impl.EmptyPanelBuilder;
import com.eviware.soapui.ui.desktop.DesktopPanel;
import com.yeremeyev.apiservant.teststeps.telegram.bot.TelegramSendBotMessageToChatTestStep;

public class TelegramSendBotMessageToChatStepPanelBuilder extends EmptyPanelBuilder<TelegramSendBotMessageToChatTestStep> {

    public TelegramSendBotMessageToChatStepPanelBuilder() {
    }

    public DesktopPanel buildDesktopPanel(TelegramSendBotMessageToChatTestStep testStep) {
        return new TelegramSendBotMessageToChatStepDesktopPanel(testStep);
    }

    public boolean hasDesktopPanel() {
        return true;
    }
}