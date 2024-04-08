package com.yeremeyev.apiservant.teststeps.readtextfile;

import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStep;
import com.eviware.soapui.impl.wsdl.teststeps.registry.WsdlTestStepFactory;

public class ReadTextFileStepFactory extends WsdlTestStepFactory {
    private static final String ID_NAME = "read-text-file";
    private static final String NAME = "Read Text File";
    private static final String DESCRIPTION = "Read simple text file content into property.";
    static final String ICON = "/read-text-file-test-step-16.png";

    public ReadTextFileStepFactory() {
        super(ID_NAME, NAME, DESCRIPTION, ICON);
    }

    public WsdlTestStep buildTestStep(WsdlTestCase testCase, TestStepConfig config, boolean forLoadTest) {
        return new ReadTextFileTestStep(testCase, config, forLoadTest);
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