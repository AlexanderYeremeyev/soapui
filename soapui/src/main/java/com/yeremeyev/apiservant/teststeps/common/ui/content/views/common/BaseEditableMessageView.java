package com.yeremeyev.apiservant.teststeps.common.ui.content.views.common;

import com.yeremeyev.apiservant.teststeps.common.ui.content.views.interfaces.EditableMessageView;
import com.yeremeyev.apiservant.teststeps.common.messages.interfaces.SimpleMessageStorage;

public abstract class BaseEditableMessageView extends BaseMessageView implements EditableMessageView {
    private boolean readOnly;

    public BaseEditableMessageView(String title, boolean readOnly, boolean active, SimpleMessageStorage messageStorage) {
        super(title, active, messageStorage);

        this.readOnly = readOnly;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void release() {
        super.release();
    }
}
