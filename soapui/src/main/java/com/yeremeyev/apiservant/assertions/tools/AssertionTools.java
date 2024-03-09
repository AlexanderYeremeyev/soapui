package com.yeremeyev.apiservant.assertions.tools;

public class AssertionTools {
    /*private void addXmlAsChildren(XmlNode node, XmlNodeReadable nodeReadable) throws XmlException {
        XmlNode currentNode = node.appendChild(nodeReadable.getName());

        Map<String, String> attributesMap = nodeReadable.getAttributesMap();
        attributesMap.entrySet().stream().forEach(entry -> currentNode.setAttribute(entry.getKey(), entry.getValue()));

        int childrensCount = nodeReadable.getChildrensCount();
        for (int index = 0; index < childrensCount; index++) {
            XmlNodeReadable child = nodeReadable.getItem(index);
            addXmlAsChildren(currentNode, child);
        }

        if (childrensCount == 0) {
            String textValue = nodeReadable.getValue();
            if (!StringTools.isNullOrEmpty(textValue)) {
                currentNode.setValue(textValue);
            }
        }
    }*/

    /*private boolean saveAssertion(XmlNode node, TestAssertion testAssertion) throws XmlException {
        if (!(testAssertion instanceof WsdlMessageAssertion)) {
            return false;
        }
        XmlNode assertionNode = node.appendChild(ConfigConstants.ASSERTION_TAG_NAME);
        assertionNode.setAttribute(ConfigConstants.TYPE_ATTRIBUTE_NAME, testAssertion.getLabel());
        assertionNode.setAttribute(ConfigConstants.ID_ATTRIBUTE_NAME, testAssertion.getId());
        assertionNode.setAttribute(ConfigConstants.NAME_ATTRIBUTE_NAME, testAssertion.getName());

        XmlNode configurationNode = assertionNode.appendChild(ConfigConstants.CONFIGURATION_TAG_NAME);

        WsdlMessageAssertion wsdlMessageAssertion = (WsdlMessageAssertion) testAssertion;
        String customConfig = wsdlMessageAssertion.getConfiguration().toString();
        XmlNodeReadable nodeReadable = XmlReader.readXml(customConfig);
        addXmlAsChildren(configurationNode, nodeReadable);

        return true;
    }*/

}
