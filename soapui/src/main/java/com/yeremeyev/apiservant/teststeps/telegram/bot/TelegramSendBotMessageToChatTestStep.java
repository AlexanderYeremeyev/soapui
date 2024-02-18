package com.yeremeyev.apiservant.teststeps.telegram.bot;

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
import com.eviware.soapui.support.xml.XmlObjectConfigurationBuilder;
import com.eviware.soapui.support.xml.XmlObjectConfigurationReader;
import com.yeremeyev.apiservant.http.tools.QueryParameterTools;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class TelegramSendBotMessageToChatTestStep extends WsdlTestStepWithProperties /*implements PropertyExpansionContainer*/ {
    private static final String CONNECTION_MISTAKE_ERROR_MESSAGE = "Connection mistake. Please choose internet access";
    private static final String CANCELLED_ERROR_MESSAGE = "Cancelled";
    private static final String SUCCESS_MESSAGE = "Success";

    private static final String EMPTY_TOKEN_ERROR_MESSAGE = "Empty token value";
    private static final String EMPTY_CHANNEL_NAME_ERROR_MESSAGE = "Empty channel name value";
    private static final String EMPTY_MESSAGE_ERROR_MESSAGE = "Empty message";

    private static final String API_TOKEN_BOT_CONFIG_ATTRIBUTE_NAME = "apiTokenBot";
    private static final String CHANNEL_NAME_CONFIG_ATTRIBUTE_NAME = "channelName";
    private static final String SEND_MESSAGE_CONFIG_ATTRIBUTE_NAME = "sendMessage";

    public static final String API_TOKEN_BOT_EXPAND_PROPERTY_NAME = "apiTokenBot";
    public static final String CHANNEL_NAME_EXPAND_PROPERTY_NAME = "channelName";
    public static final String SEND_MESSAGE_EXPAND_PROPERTY_NAME = "sendMessage";
    public static final String RESPONSE_MESSAGE_EXPAND_PROPERTY_NAME = "responseMessage";

    private String apiTokenBot;
    private String channelName;
    private String sendMessage;

    private boolean canceled;
    private boolean error;
    private String errorMessage;

    private String responseMessage;

    private void saveConfig(TestStepConfig config) {
        XmlObjectConfigurationBuilder builder = new XmlObjectConfigurationBuilder();

        builder.addAttribute(API_TOKEN_BOT_CONFIG_ATTRIBUTE_NAME, apiTokenBot);
        builder.addAttribute(CHANNEL_NAME_CONFIG_ATTRIBUTE_NAME, channelName);
        builder.addAttribute(SEND_MESSAGE_CONFIG_ATTRIBUTE_NAME, sendMessage);

        config.setConfig(builder.finish());
    }

    private void readConfig(TestStepConfig config) {
        XmlObjectConfigurationReader reader = new XmlObjectConfigurationReader(config.getConfig());

        apiTokenBot = reader.readAttribute(API_TOKEN_BOT_CONFIG_ATTRIBUTE_NAME, StringUtils.EMPTY);
        channelName = reader.readAttribute(CHANNEL_NAME_CONFIG_ATTRIBUTE_NAME, StringUtils.EMPTY);
        sendMessage = reader.readAttribute(SEND_MESSAGE_CONFIG_ATTRIBUTE_NAME, StringUtils.EMPTY);
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

    public TelegramSendBotMessageToChatTestStep(WsdlTestCase testCase, TestStepConfig config, boolean forLoadTest) {
        super(testCase, config, true, forLoadTest);

        apiTokenBot = StringUtils.EMPTY;
        channelName = StringUtils.EMPTY;
        sendMessage = StringUtils.EMPTY;

        if (!forLoadTest) {
            setIcon(UISupport.createImageIcon(TelegramSendBotMessageToChatStepFactory.ICON));
        }

        initializeConfig(config, forLoadTest);

        /*addProperty(new DefaultTestStepProperty("delay", true, new DefaultTestStepProperty.PropertyHandlerAdapter() {

            @Override
            public String getValue(DefaultTestStepProperty property) {
                return getDelayString();
            }

            @Override
            public void setValue(DefaultTestStepProperty property, String value) {
                setDelayString(value);
            }
        }, this));*/
    }

    @Override
    public int getTypeId() {
        return ModelItemType.TELEGRAM_SEND_BOT_MESSAGE_TEST_STEP.getId();
    }

    /*public PropertyExpansion[] getPropertyExpansions() {
        List<PropertyExpansion> result = new ArrayList<PropertyExpansion>();
        result.addAll(PropertyExpansionUtils.extractPropertyExpansions(this, this, "delayString"));
        return result.toArray(new PropertyExpansion[result.size()]);
    }*/

    @Override
    public void resetConfigOnMove(TestStepConfig config) {
        super.resetConfigOnMove(config);
        readConfig(config);
    }

    public TestStepResult run(TestCaseRunner testRunner, TestCaseRunContext context) {
        WsdlTestStepResult result = new WsdlTestStepResult(this);

        String apiTokenBotExpandValue = context.expand(apiTokenBot);
        String channelNameExpandValue = context.expand(channelName);
        String sendMessageExpandValue = context.expand(sendMessage);
        sendMessageExpandValue = QueryParameterTools.encode(sendMessageExpandValue);

        result.startTimer();

        HttpResponse response = null;
        errorMessage = StringUtils.EMPTY;
        try {
            canceled = false;
            error = false;

            validateParameters(apiTokenBotExpandValue, channelNameExpandValue, sendMessageExpandValue);

            String urlString = String.format(
                    "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
                    apiTokenBotExpandValue,
                    channelNameExpandValue,
                    sendMessageExpandValue
            );
            // https://api.telegram.org/bot[API_TOKEN_BOT]/sendMessage?chat_id=[CHANNEL_NAME]&text=Test
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .timeout(Duration.of(30, SECONDS))
                    .GET()
                    .build();
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception exception) {
            loggingMistake(exception);
        }

        result.stopTimer();
        result.setStatus(calculateStatus(response));

        String responseMessage = error ? errorMessage : response.body().toString();
        if (error) {
            testRunner.fail(responseMessage);
        } else if (canceled) {
            testRunner.cancel(CANCELLED_ERROR_MESSAGE);
        } else {
            ((AbstractMockTestRunner) testRunner).getLog().info(SUCCESS_MESSAGE);
        }
        setResponseMessage(responseMessage);

        return result;
    }

    private void validateParameters(String apiTokenBotValue, String channelNameValue, String sendMessageValue) throws Exception {
        if (StringUtils.isNullOrEmpty(apiTokenBotValue)) {
            throw new Exception(EMPTY_TOKEN_ERROR_MESSAGE);
        }
        if (StringUtils.isNullOrEmpty(channelNameValue)) {
            throw new Exception(EMPTY_CHANNEL_NAME_ERROR_MESSAGE);
        }
        if (StringUtils.isNullOrEmpty(sendMessageValue)) {
            throw new Exception(EMPTY_MESSAGE_ERROR_MESSAGE);
        }
    }

    private void loggingMistake(Exception exception) {
        error = true;
        if (exception instanceof ConnectException) {
            errorMessage = CONNECTION_MISTAKE_ERROR_MESSAGE;
        } else {
            errorMessage = exception.getMessage();
        }
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

    private TestStepResult.TestStepStatus calculateStatus(HttpResponse response) {
        if (error || response.statusCode() != 200) {
            return TestStepResult.TestStepStatus.FAILED;
        }
        return canceled ? TestStepResult.TestStepStatus.CANCELED : TestStepResult.TestStepStatus.OK;
    }

    public String getApiTokenBot() {
        return apiTokenBot;
    }

    public void setApiTokenBot(String apiTokenBot) {
        this.apiTokenBot = apiTokenBot;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        String oldResponseMessage = this.responseMessage;
        this.responseMessage = responseMessage;

        notifyPropertyChanged(RESPONSE_MESSAGE_EXPAND_PROPERTY_NAME, oldResponseMessage, responseMessage);
    }
}