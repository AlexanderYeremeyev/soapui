package com.yeremeyev.apiservant.teststeps.common.ui.content.views.common;

import com.yeremeyev.apiservant.teststeps.common.ui.content.views.interfaces.MessageView;
import com.yeremeyev.apiservant.teststeps.common.messages.interfaces.SimpleMessageStorage;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public abstract class BaseMessageView
        extends JPanel
        implements MessageView {
    private String title;
    private boolean active;
    private SimpleMessageStorage messageStorage;

    public BaseMessageView(String title, boolean active, SimpleMessageStorage messageStorage) {
        this.title = title;
        this.active = active;
        this.messageStorage = messageStorage;

        setBorder(BorderFactory.createEmptyBorder());
        setLayout(new BorderLayout());
    }

    @Override
    public String getViewTitle() {
        return title;
    }

    @Override
    public void setActive(boolean active) {
        if (this.active != active) {
            this.active = active;
            if (this.active) {
                onMessageUpdated();
            }
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void release() {
        messageStorage = null;
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    protected SimpleMessageStorage getMessageStorage() {
        return messageStorage;
    }

    protected abstract void onMessageUpdated();
}
