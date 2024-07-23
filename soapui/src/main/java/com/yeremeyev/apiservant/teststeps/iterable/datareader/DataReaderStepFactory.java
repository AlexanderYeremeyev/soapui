package com.yeremeyev.apiservant.teststeps.iterable.datareader;

import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStep;
import com.eviware.soapui.impl.wsdl.teststeps.registry.WsdlTestStepFactory;

public class DataReaderStepFactory extends WsdlTestStepFactory {
    private static final String ID_NAME = "iterable-data-reader";
    private static final String NAME = "Iterable Data Reader";
    private static final String DESCRIPTION = "Read data collection step by step";
    static final String ICON = "/iterable-data-reader-test-step-16.png";

    public DataReaderStepFactory() {
        super(ID_NAME, NAME, DESCRIPTION, ICON);
    }

    public WsdlTestStep buildTestStep(WsdlTestCase testCase, TestStepConfig config, boolean forLoadTest) {
        return new DataReaderTestStep(testCase, config, forLoadTest);
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