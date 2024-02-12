package com.yeremeyev.apiservant.teststeps.telegram.bot.ui;

import com.eviware.soapui.impl.wsdl.panels.support.MockTestRunContext;
import com.eviware.soapui.impl.wsdl.panels.support.MockTestRunner;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStepResult;
import com.eviware.soapui.support.UISupport;
import com.yeremeyev.apiservant.teststeps.telegram.bot.TelegramSendBotMessageToChatTestStep;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;

class RunAction extends AbstractAction {
    private TelegramSendBotMessageToChatTestStep testStep;
    private Thread runThread;

    public RunAction(TelegramSendBotMessageToChatTestStep testStep) {
        this.testStep = testStep;

        putValue(Action.SMALL_ICON, UISupport.createImageIcon("/run.png"));
        putValue(Action.SHORT_DESCRIPTION, "Runs this script in a seperate thread using a mock testRunner and testContext");
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        setEnabled(false);

        runThread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                MockTestRunner mockRunner = new MockTestRunner(testStep.getTestCase());
                MockTestRunContext mockContext = new MockTestRunContext(mockRunner, testStep);
                WsdlTestStepResult result = (WsdlTestStepResult) testStep.run(mockRunner, mockContext);

                Throwable throwable = result.getError();
                if (throwable != null) {
                    UISupport.showErrorMessage(throwable.toString());
                }
            } finally {
                runThread = null;
                setEnabled(true);
            }
        });

        runThread.start();
    }
}
