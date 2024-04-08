package com.yeremeyev.apiservant.teststeps.common.actions;

import com.eviware.soapui.impl.wsdl.panels.support.MockTestRunContext;
import com.eviware.soapui.impl.wsdl.panels.support.MockTestRunner;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStep;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStepResult;
import com.eviware.soapui.support.UISupport;
import org.apache.logging.log4j.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;

public class RunAction extends AbstractAction {
    private Logger logger;
    private WsdlTestStep testStep;
    private Thread runThread;

    public RunAction(WsdlTestStep testStep, Logger logger) {
        this.logger = logger;
        this.testStep = testStep;

        putValue(Action.SMALL_ICON, UISupport.createImageIcon("/run.png"));
        putValue(Action.SHORT_DESCRIPTION, "Run test step");
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
