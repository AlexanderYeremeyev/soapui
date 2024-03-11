package com.yeremeyev.apiservant.teststeps.common.messages.interfaces;

public interface SimpleMessageStorage {

    public void setMessage(String message);

    public String getMessage();

    public void addListener(UpdateSimpleMessageListener updateMessageListener);

    public void removeListener(UpdateSimpleMessageListener updateMessageListener);
}
