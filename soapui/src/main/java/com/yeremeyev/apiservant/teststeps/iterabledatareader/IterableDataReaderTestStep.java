package com.yeremeyev.apiservant.teststeps.iterabledatareader;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.panels.support.AbstractMockTestRunner;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStepResult;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStepWithProperties;
import com.eviware.soapui.model.ModelItemType;
import com.eviware.soapui.model.propertyexpansion.PropertyExpander;
import com.eviware.soapui.model.support.DefaultTestStepProperty;
import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestStepResult;
import com.eviware.soapui.support.StringUtils;
import com.eviware.soapui.support.UISupport;
import com.yeremeyev.apiservant.configs.ConfigConstants;
import com.yeremeyev.apiservant.teststeps.readtextfile.ReadTextFileStepFactory;
import com.yeremeyev.java.core.tools.files.FileTools;
import com.yeremeyev.java.core.tools.languages.xml.creator.XmlNode;
import com.yeremeyev.java.core.tools.languages.xml.exceptions.XmlException;
import com.yeremeyev.java.core.tools.languages.xml.reader.XmlNodeReadable;
import com.yeremeyev.java.core.tools.languages.xml.reader.XmlReader;
import org.apache.xmlbeans.XmlObject;

import java.nio.charset.StandardCharsets;

public class IterableDataReaderTestStep extends WsdlTestStepWithProperties {
    private static final String CANCELLED_ERROR_MESSAGE = "Cancelled";
    private static final String SUCCESS_MESSAGE = "Success";

    private static final String SOURCE_TYPE_CONFIG_ATTRIBUTE_NAME = "type";

    //public static final String FILE_CONTENT_EXPAND_PROPERTY_NAME = "fileContent";

    //public static final String FILE_CONTENT_USER_VISIBLE_PROPERTY_NAME = "File Content";

    private IterableDataSourceType sourceType;

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

            String sourceTypeValue = configNode.getAttribute(SOURCE_TYPE_CONFIG_ATTRIBUTE_NAME, IterableDataSourceType.MANUAL_GRID.getType());
            sourceType = IterableDataSourceType.valueOf(sourceTypeValue);
        } catch (XmlException exception) {
            SoapUI.logError(exception);
        }
    }

    private void initializeConfig(TestStepConfig config, boolean forLoadTest) {
        if (config.getConfig() == null) {
            if (!forLoadTest) {
                saveConfig(config);
            }
        } else {
            readConfig(config);
        }
    }

    public IterableDataReaderTestStep(WsdlTestCase testCase, TestStepConfig config, boolean forLoadTest) {
        super(testCase, config, true, forLoadTest);

        if (!forLoadTest) {
            setIcon(UISupport.createImageIcon(IterableDataReaderStepFactory.ICON));
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

        String filePathValue = PropertyExpander.expandProperties(filePath);

        result.startTimer();

        errorMessage = StringUtils.EMPTY;
        String content = null;
        try {
            canceled = false;
            error = false;

            validateParameters(filePathValue);

            byte[] binaryContent = FileTools.readyFile(filePathValue);
            if (binaryContent == null) {
                error = true;
                errorMessage = CANNOT_READ_FILE_ERROR_MESSAGE;
            } else {
                content = new String(binaryContent, StandardCharsets.UTF_8);
            }
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
        setFileContent(content);

        return result;
    }

    private void validateParameters(String filePathValue) throws Exception {
        if (StringUtils.isNullOrEmpty(filePathValue)) {
            throw new Exception(EMPTY_FILE_PATH_ERROR_MESSAGE);
        }
        if (!FileTools.exist(filePathValue)) {
            throw new Exception(FILE_IS_ABSENT_ERROR_MESSAGE);
        }
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String content) {
        String oldFileContent = this.fileContent;
        this.fileContent = content;

        notifyPropertyChanged(FILE_CONTENT_EXPAND_PROPERTY_NAME, oldFileContent, fileContent);
    }
}