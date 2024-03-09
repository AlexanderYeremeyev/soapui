package com.yeremeyev.apiservant.teststeps.common.messages;

import com.yeremeyev.apiservant.teststeps.common.messages.interfaces.SimpleMessageStorage;
import com.yeremeyev.apiservant.teststeps.common.messages.interfaces.UpdateSimpleMessageListener;
import com.yeremeyev.java.core.interfaces.common.Releasable;

import java.util.ArrayList;
import java.util.List;

public class BaseMessageStorage implements SimpleMessageStorage, Releasable {
    private String message;
    private List<UpdateSimpleMessageListener> listenersList;

    public BaseMessageStorage() {
        listenersList = new ArrayList<>();
    }

    @Override
    public void setMessage(String message) {
        this.message = message;

        listenersList.stream().forEach(item -> item.onMessageSimpleUpdate(message));
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void addListener(UpdateSimpleMessageListener updateMessageListener) {
        listenersList.add(updateMessageListener);
    }

    @Override
    public void removeListener(UpdateSimpleMessageListener updateMessageListener) {
        listenersList.remove(updateMessageListener);
    }

    @Override
    public void release() {
        listenersList.clear();
    }
}
