package com.yeremeyev.apiservant.teststeps.telegram.bot;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.config.TestAssertionConfig;
import com.eviware.soapui.impl.wsdl.support.assertions.AssertableConfig;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlMessageAssertion;
import com.eviware.soapui.impl.wsdl.teststeps.assertions.TestAssertionRegistry;
import com.eviware.soapui.model.ModelItem;
import com.eviware.soapui.model.iface.Interface;
import com.eviware.soapui.model.testsuite.Assertable;
import com.eviware.soapui.model.testsuite.AssertionsListener;
import com.eviware.soapui.model.testsuite.TestAssertion;
import com.eviware.soapui.model.testsuite.TestStep;
import com.eviware.soapui.support.ModelItemNamer;
import com.eviware.soapui.support.UISupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TelegramSendBotMessageAssertableAdapter implements Assertable {
    private List<AssertionsListener> listenersList;
    private List<TestAssertion> assertionList;
    private TelegramSendBotMessageToChatTestStep testStep;

    public TelegramSendBotMessageAssertableAdapter(
            TelegramSendBotMessageToChatTestStep testStep
    )
    {
        this.testStep = testStep;
        listenersList = new ArrayList<>();
        assertionList = new ArrayList<>();
    }

    @Override
    public TestAssertion addAssertion(String assertionLabel) {
        try {
            //TestAssertionConfig assertionConfig = assertableConfig.addNewAssertion();
            TestAssertionConfig assertionConfig = TestAssertionConfig.Factory.newInstance();
            assertionConfig.setType(TestAssertionRegistry.getInstance().getAssertionTypeForName(assertionLabel));

            String name = assertionLabel;
            while (getAssertionByName(name.trim()) != null) {
                name = UISupport.prompt(
                        "Specify unique name of Assertion",
                        "Rename Assertion",
                        assertionLabel
                                + " "
                                /*+ (getAssertionsOfType(TestAssertionRegistry.getInstance().getAssertionClassType(
                                assertionConfig)).size())*/);
                if (name == null) {
                    if (UISupport.isUsingConsoleDialogs()) {
                        name = ModelItemNamer.createName(assertionLabel, assertionList);
                    } else {
                        return null;
                    }
                }
            }
            WsdlMessageAssertion assertion = addWsdlAssertion(assertionConfig);
            if (assertion == null) {
                return null;
            }

            assertionConfig.setName(name);
            assertion.updateConfig(assertionConfig);

            fireAssertionAdded(assertion);

            return assertion;
        } catch (Exception e) {
            SoapUI.logError(e);
            return null;
        }
    }

    private WsdlMessageAssertion addWsdlAssertion(TestAssertionConfig config) {
        try {
            WsdlMessageAssertion assertion = TestAssertionRegistry.getInstance().buildAssertion(config, this);
            if (assertion == null) {
                return null;
            } else {
                assertionList.add(assertion);
                //assertion.addPropertyChangeListener(this);

                return assertion;
            }
        } catch (Exception e) {
            SoapUI.logError(e);
            return null;
        }
    }

    @Override
    public void addAssertionsListener(AssertionsListener listener) {
        listenersList.add(listener);
    }

    @Override
    public void removeAssertionsListener(AssertionsListener listener) {
        listenersList.remove(listener);
    }

    @Override
    public int getAssertionCount() {
        return assertionList.size();
    }

    @Override
    public TestAssertion getAssertionAt(int index) {
        if (index < 0 || assertionList.size() <= index) {
            return null;
        }
        return assertionList.get(index);
    }

    @Override
    public void removeAssertion(TestAssertion assertion) {
        assertionList.remove(assertion);
    }

    @Override
    public AssertionStatus getAssertionStatus() {
        return null;
    }

    @Override
    public String getAssertableContentAsXml() {
        return testStep.getResponseMessage();
    }

    @Override
    public String getAssertableContent() {
        return testStep.getResponseMessage();
    }

    @Override
    public String getDefaultAssertableContent() {
        return null;
    }

    @Override
    public TestAssertionRegistry.AssertableType getAssertableType() {
        return TestAssertionRegistry.AssertableType.RESPONSE;
    }

    @Override
    public List<TestAssertion> getAssertionList() {
        return assertionList;
    }

    @Override
    public TestAssertion getAssertionByName(String name) {
        for (TestAssertion testAssertion : assertionList) {
            if (testAssertion.getName().equals(name)) {
                return testAssertion;
            }
        }
        return null;
    }

    @Override
    public ModelItem getModelItem() {
        return testStep;
    }

    @Override
    public TestStep getTestStep() {
        return testStep;
    }

    @Override
    public Interface getInterface() {
        return null;
    }

    @Override
    public TestAssertion cloneAssertion(TestAssertion source, String name) {
        return null;
    }

    @Override
    public Map<String, TestAssertion> getAssertions() {
        return null;
    }

    @Override
    public TestAssertion moveAssertion(int ix, int offset) {
        return null;
    }

    //--------
    public void fireAssertionAdded(WsdlMessageAssertion assertion) {
        AssertionsListener[] listeners = listenersList.toArray(new AssertionsListener[listenersList.size()]);

        for (int c = 0; c < listeners.length; c++) {
            listeners[c].assertionAdded(assertion);
        }    }

    public void fireAssertionRemoved(WsdlMessageAssertion assertion) {
        AssertionsListener[] listeners = listenersList.toArray(new AssertionsListener[listenersList.size()]);

        for (int c = 0; c < listeners.length; c++) {
            listeners[c].assertionRemoved(assertion);
        }
    }

    public void fireAssertionMoved(WsdlMessageAssertion assertion, int ix, int offset) {
        AssertionsListener[] listeners = listenersList.toArray(new AssertionsListener[listenersList.size()]);

        for (int c = 0; c < listeners.length; c++) {
            listeners[c].assertionMoved(assertion, ix, offset);
        }
    }

}
