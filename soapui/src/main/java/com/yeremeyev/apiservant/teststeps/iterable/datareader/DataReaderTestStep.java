package com.yeremeyev.apiservant.teststeps.iterable.datareader;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.panels.support.AbstractMockTestRunner;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStepResult;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStepWithProperties;
import com.eviware.soapui.model.ModelItemType;
import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestStepResult;
import com.eviware.soapui.support.StringUtils;
import com.eviware.soapui.support.UISupport;
import com.yeremeyev.apiservant.configs.ConfigConstants;
import com.yeremeyev.java.core.tools.languages.xml.creator.XmlNode;
import com.yeremeyev.java.core.tools.languages.xml.exceptions.XmlException;
import com.yeremeyev.java.core.tools.languages.xml.reader.XmlNodeReadable;
import com.yeremeyev.java.core.tools.languages.xml.reader.XmlReader;
import org.apache.xmlbeans.XmlObject;

public class DataReaderTestStep extends WsdlTestStepWithProperties {
    private static final String CANCELLED_ERROR_MESSAGE = "Cancelled";
    private static final String SUCCESS_MESSAGE = "Success";
    private static final DataSourceType DEFAULT_DATA_SOURCE = DataSourceType.MANUAL_GRID;

    private static final String SOURCE_TYPE_CONFIG_ATTRIBUTE_NAME = "type";

    private DataSourceType sourceType;

    private boolean canceled;
    private boolean error;
    private String errorMessage;

    private void saveConfig(TestStepConfig config) {

        try {
            XmlNode xmlConfigNode = new XmlNode(ConfigConstants.CONFIGURATION_TAG_NAME);
            xmlConfigNode.setAttribute(SOURCE_TYPE_CONFIG_ATTRIBUTE_NAME, sourceType.getType());
            String resultXml = xmlConfigNode.toXml();

            XmlObject xmlObject = XmlObject.Factory.parse(resultXml);

            config.setConfig(xmlObject);
        } catch (XmlException | org.apache.xmlbeans.XmlException createXmlException) {
            SoapUI.logError(createXmlException);
        }
    }

    private void readConfig(TestStepConfig config) {
        XmlObject stepConfig = config.getConfig();
        try {
            String xmlText = stepConfig.xmlText();
            XmlNodeReadable configNode = XmlReader.readXml(xmlText);

            String sourceTypeValue = configNode.getAttribute(SOURCE_TYPE_CONFIG_ATTRIBUTE_NAME, DataSourceType.MANUAL_GRID.getType());
            sourceType = DataSourceType.fromType(sourceTypeValue, DEFAULT_DATA_SOURCE);
        } catch (XmlException exception) {
            SoapUI.logError(exception);
        }
    }

    private void initializeConfig(TestStepConfig config, boolean forLoadTest) {
        sourceType = DEFAULT_DATA_SOURCE;
        if (config.getConfig() == null) {
            if (!forLoadTest) {
                saveConfig(config);
            }
        } else {
            readConfig(config);
        }
    }

    public DataReaderTestStep(WsdlTestCase testCase, TestStepConfig config, boolean forLoadTest) {
        super(testCase, config, true, forLoadTest);

        if (!forLoadTest) {
            setIcon(UISupport.createImageIcon(DataReaderStepFactory.ICON));
        }

        initializeConfig(config, forLoadTest);
    }

    @Override
    public int getTypeId() {
        return ModelItemType.ITERABLE_DATA_READER_TEST_STEP.getId();
    }

    @Override
    public void resetConfigOnMove(TestStepConfig config) {
        super.resetConfigOnMove(config);
        readConfig(config);
    }

    public TestStepResult run(TestCaseRunner testRunner, TestCaseRunContext context) {
        WsdlTestStepResult result = new WsdlTestStepResult(this);

        //String filePathValue = PropertyExpander.expandProperties(filePath);

        result.startTimer();

        errorMessage = StringUtils.EMPTY;
        try {
            canceled = false;
            error = false;

        } catch (Exception exception) {
            loggingMistake(exception);
        }

        result.stopTimer();
        result.setStatus(calculateStatus());

        if (error) {
            testRunner.fail(errorMessage);
        } else if (canceled) {
            testRunner.cancel(CANCELLED_ERROR_MESSAGE);
        } else {
            ((AbstractMockTestRunner) testRunner).getLog().info(SUCCESS_MESSAGE);
        }
        return result;
    }

    private void loggingMistake(Exception exception) {
        error = true;
        errorMessage = exception.getMessage();
        SoapUI.logError(exception);
    }

    @Override
    public void beforeSave() {
        super.beforeSave();

        saveConfig(getConfig());
    }

    @Override
    public boolean cancel() {
        canceled = true;
        return true;
    }

    private TestStepResult.TestStepStatus calculateStatus() {
        if (error) {
            return TestStepResult.TestStepStatus.FAILED;
        }
        return canceled ? TestStepResult.TestStepStatus.CANCELED : TestStepResult.TestStepStatus.OK;
    }
}