package com.yeremeyev.apiservant.teststeps.telegram.bot;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.config.TestStepConfig;
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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class TelegramSendBotMessageToChatTestStep extends WsdlTestStepWithProperties /*implements PropertyExpansionContainer*/ {
    private static final String API_TOKEN_BOT_CONFIG_PROPERTY_NAME = "api-token-bot";
    private static final String CHANNEL_NAME_CONFIG_PROPERTY_NAME = "channel-name";
    private static final String SEND_MESSAGE_CONFIG_PROPERTY_NAME = "send-message";

    public static final String API_TOKEN_BOT_EXPAND_PROPERTY_NAME = "apiTokenBot";
    public static final String CHANNEL_NAME_EXPAND_PROPERTY_NAME = "channelName";
    public static final String SEND_MESSAGE_EXPAND_PROPERTY_NAME = "sendMessage";
    public static final String RESPONSE_MESSAGE_EXPAND_PROPERTY_NAME = "responseMessage";

    private String apiTokenBot;
    private String channelName;
    private String sendMessage;

    private boolean canceled;
    private boolean error;

    private String responseMessage;

    private void saveConfig(TestStepConfig config) {
        XmlObjectConfigurationBuilder builder = new XmlObjectConfigurationBuilder();
        builder.add(API_TOKEN_BOT_CONFIG_PROPERTY_NAME, apiTokenBot);
        builder.add(CHANNEL_NAME_CONFIG_PROPERTY_NAME, channelName);
        builder.add(SEND_MESSAGE_CONFIG_PROPERTY_NAME, sendMessage);
        config.setConfig(builder.finish());
    }

    private void readConfig(TestStepConfig config) {
        XmlObjectConfigurationReader reader = new XmlObjectConfigurationReader(config.getConfig());
        apiTokenBot = reader.readString(API_TOKEN_BOT_CONFIG_PROPERTY_NAME, StringUtils.EMPTY);
        channelName = reader.readString(CHANNEL_NAME_CONFIG_PROPERTY_NAME, StringUtils.EMPTY);
        sendMessage = reader.readString(SEND_MESSAGE_CONFIG_PROPERTY_NAME, StringUtils.EMPTY);
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

    /*public void setDelayString(String delayString) {
        if (this.delayString.equals(delayString)) {
            return;
        }

        String oldLabel = getLabel();

        this.delayString = delayString;
        saveDelay(getConfig());
        notifyPropertyChanged(WsdlTestStep.LABEL_PROPERTY, oldLabel, getLabel());
        // FIXME This should not be hard coded
        firePropertyValueChanged("delay", oldLabel, getLabel());
    }*/

    /*public String getDelayString() {
        return delayString;
    }*/

    /*public int getDelay() {
        try {
            return Integer.parseInt(PropertyExpander.expandProperties(this, delayString));
        } catch (NumberFormatException e) {
            return -1;
        }
    }*/

    /*public void setDelay(int delay) {
        String oldLabel = getLabel();

        this.delayString = String.valueOf(delay);
        saveDelay(getConfig());
        notifyPropertyChanged(WsdlTestStep.LABEL_PROPERTY, oldLabel, getLabel());
        firePropertyValueChanged("delay", oldLabel, getLabel());
    }*/

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        String oldResponseMessage = this.responseMessage;
        this.responseMessage = responseMessage;

        notifyPropertyChanged(RESPONSE_MESSAGE_EXPAND_PROPERTY_NAME, oldResponseMessage, responseMessage);
        //RESPONSE_MESSAGE_EXPAND_PROPERTY_NAME
    }

    public TestStepResult run(TestCaseRunner testRunner, TestCaseRunContext context) {
        WsdlTestStepResult result = new WsdlTestStepResult(this);
        result.startTimer();

        HttpResponse response = null;
        try {
            canceled = false;
            error = false;

            String urlString = String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s", apiTokenBot, channelName, sendMessage);
            // https://api.telegram.org/bot[API_TOKEN_BOT]/sendMessage?chat_id=[CHANNEL_NAME]&text=Test
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .timeout(Duration.of(30, SECONDS))
                    .GET()
                    .build();
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException exception) {
            error = true;
            SoapUI.logError(exception);
        }

        result.stopTimer();
        result.setStatus(calculateStatus(response.statusCode()));

        setResponseMessage(response.body().toString());

        return result;
    }

    @Override
    public boolean cancel() {
        canceled = true;
        return true;
    }

    private TestStepResult.TestStepStatus calculateStatus(int statusCode) {
        if (error || statusCode != 200) {
            return TestStepResult.TestStepStatus.FAILED;
        }
        return canceled ? TestStepResult.TestStepStatus.CANCELED : TestStepResult.TestStepStatus.OK;
    }
}