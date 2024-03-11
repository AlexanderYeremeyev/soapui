package com.yeremeyev.apiservant.teststeps.telegram.bot.ui;

import com.eviware.soapui.impl.wsdl.panels.support.MockTestRunContext;
import com.eviware.soapui.impl.wsdl.panels.support.MockTestRunner;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStepResult;
import com.eviware.soapui.support.UISupport;
import com.yeremeyev.apiservant.teststeps.telegram.bot.TelegramSendBotMessageToChatTestStep;
import org.apache.logging.log4j.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;

class RunAction extends AbstractAction {
    private Logger logger;
    private TelegramSendBotMessageToChatTestStep testStep;
    private Thread runThread;

    public RunAction(TelegramSendBotMessageToChatTestStep testStep, Logger logger) {
        this.logger = logger;
        this.testStep = testStep;

        putValue(Action.SMALL_ICON, UISupport.createImageIcon("/run.png"));
        putValue(Action.SHORT_DESCRIPTION, "Runs this script in a seperate thread using a mock testRunner and testContext");
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        setEnabled(false);

        runThread = new Thread(() -> {
            try {
                MockTestRunner mockRunner = new MockTestRunner(testStep.getTestCase(), logger);
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
