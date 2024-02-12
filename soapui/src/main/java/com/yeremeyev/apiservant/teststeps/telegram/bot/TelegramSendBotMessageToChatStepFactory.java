package com.yeremeyev.apiservant.teststeps.telegram.bot;

import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStep;
import com.eviware.soapui.impl.wsdl.teststeps.registry.WsdlTestStepFactory;

public class TelegramSendBotMessageToChatStepFactory extends WsdlTestStepFactory {
    private static final String ID_NAME = "tg-sent-bot-message-to-chat";
    private static final String NAME = "Telegram Send Bot Message";
    private static final String DESCRIPTION = "Send message from bot to specific chat.";
    static final String ICON = "/telegram-test-step-16.png";

    public TelegramSendBotMessageToChatStepFactory() {
        super(ID_NAME, NAME, DESCRIPTION, ICON);
    }

    public WsdlTestStep buildTestStep(WsdlTestCase testCase, TestStepConfig config, boolean forLoadTest) {
        return new TelegramSendBotMessageToChatTestStep(testCase, config, forLoadTest);
    }

    @Override
    public TestStepConfig createNewTestStep(WsdlTestCase testCase, String name) {
        TestStepConfig testStepConfig = TestStepConfig.Factory.newInstance();
        testStepConfig.setType(ID_NAME);
        testStepConfig.setName(name);
        return testStepConfig;
    }

    @Override
    public boolean canCreate() {
        return true;
    }
}