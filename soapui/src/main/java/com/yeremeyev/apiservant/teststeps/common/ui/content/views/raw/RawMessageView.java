package com.yeremeyev.apiservant.teststeps.common.ui.content.views.raw;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.settings.UISettings;
import com.yeremeyev.apiservant.teststeps.common.messages.interfaces.SimpleMessageStorage;
import com.yeremeyev.apiservant.teststeps.common.messages.interfaces.UpdateSimpleMessageListener;
import com.yeremeyev.apiservant.teststeps.common.ui.content.views.common.BaseMessageView;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class RawMessageView extends BaseMessageView implements UpdateSimpleMessageListener {
    private static final String DEFAULT_TITLE = "Raw";

    private JTextArea textArea;
    private JScrollPane scrollPane;

    private void buildUI() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(SoapUI.getSettings().getBoolean(UISettings.WRAP_RAW_MESSAGES));
        String initialText = getMessageStorage().getMessage();
        textArea.setText(initialText);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane);
    }

    public RawMessageView(SimpleMessageStorage messageStorage) {
        super(DEFAULT_TITLE, false, messageStorage);

        buildUI();

        getMessageStorage().addListener(this);
    }

    @Override
    protected void onMessageUpdated() {
        textArea.setText(getMessageStorage().getMessage());
        textArea.setCaretPosition(0);
    }

    @Override
    public void release() {
        getMessageStorage().removeListener(this);
        super.release();
    }

    @Override
    public void onMessageSimpleUpdate(String message) {
        String currentMessage = textArea.getText();
        if (currentMessage.equals(message)) {
            return;
        }
        textArea.setText(message);
    }
}
