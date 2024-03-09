package com.yeremeyev.apiservant.teststeps.common.ui.content.views;

import com.yeremeyev.apiservant.teststeps.common.ui.content.views.interfaces.MessageView;
import com.yeremeyev.apiservant.teststeps.common.ui.content.views.json.JsonMessageView;
import com.yeremeyev.apiservant.teststeps.common.ui.content.views.raw.RawMessageView;
import com.yeremeyev.apiservant.teststeps.common.ui.content.views.text.TextView;
import com.yeremeyev.apiservant.teststeps.common.messages.BaseMessageStorage;

public class ViewFactory {

    public static MessageView create(PossibleViews viewType, BaseMessageStorage messageStorage, boolean readOnly) {
        switch (viewType) {
            case TEXT:
                return new TextView(messageStorage);
            case RAW:
                return new RawMessageView(messageStorage);
            case JSON:
                return new JsonMessageView(readOnly, messageStorage);
        }
        return null;
    }
}
