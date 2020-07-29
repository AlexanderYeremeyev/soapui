package com.eviware.soapui.model;

public enum ModelItemType {
    UNKNOWN(0, "unknown", "it's customer model item. Or developer mistake."),
    WORKSPACE(1, "workspace", "navigator tree workspace"),
    PROJECT(2, "project", "navigator tree project"),

    WSDL_SERVICE(3, "wsdl service", "traditional wsdl service node."),
    REST_SERVICE(4, "rest service", "rest service node."),

    TEST_SUITE(5, "test suite", "test suite node."),
    TEST_CASE(6, "test case", "test case node"),
    TEST_STEPS(7, "test steps", "test steps is a pseudo node between testcase and usual test steps"),
    LOAD_TESTS(8, "load tests", "load tests is a pseudo node between testcase and load test items"),
    SECURITY_TESTS(9, "security tests", "security tests is a pseudo node between testcase and security test items"),
    SECURE_TEST(10, "secure test", "secure test node."),
    LOADUI_TEST(11, "loadui test", "loadui test node."),

    WSDL_OPERATION(12, "WSDL operation", "wsdl interface may have a few wsdl operations as children."),
    REST_RESOURCE(13, "REST resource", "rest interface may have a few rest resources as children."),
    REST_METHOD(14, "REST method", "rest resource has rest method child."),

    WSDL_MOCK_SERVICE(15, "wsdl mock service", "wsdl mock service"),
    REST_MOCK_SERVICE(16, "rest mock service", "rest mock service"),

    SOAP_REQUEST_TEST_STEP(17, "SOAP request test step", "SOAP request test step"),
    REST_REQUEST_TEST_STEP(18, "Rest test request step", "Rest test request step"),
    HTTP_REQUEST_TEST_STEP(19, "Http test request test step", "Http test request test step"),
    AMF_REQUEST_TEST_STEP(20, "AMF request test step", "AMF request test step"),
    JDBC_REQUEST_TEST_STEP(21, "Jdbc request test step", "Jdbc request test step"),
    PROPERTIES_TEST_STEP(22, "Properties test step", "Properties test step"),
    PROPERTY_TRANSFERS_TEST_STEP(23, "Property transfers test step", "Property transfers test step"),
    GOTO_TEST_STEP(24, "Wsdl goto test step", "Wsdl goto test step"),
    RUN_TESTCASE_TEST_STEP(25, "Run testcase test step", "Run testcase test step"),
    GROOVY_SCRYPT_TEST_STEP(26, "Debuggable wsdl groovy scrypt test step", "Debuggable wsdl groovy scrypt test step"),
    DELAY_TEST_STEP(27, "Wsdl delay test step", "Wsdl delay test step"),
    SOAP_MOCK_RESPONSE_TEST_STEP(28, "SOAP mock response test step", "SOAP mock response test step"),
    MANUAL_TEST_STEP(29, "Manual test step", "Manual test step");
    // LAST ID = 29

    private int id;
    private String name;
    private String description;

    private ModelItemType(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static ModelItemType get(int id) {
        for (ModelItemType item : values()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return ModelItemType.UNKNOWN;
    }

    public static boolean isService(ModelItemType modelItemType) {
        boolean isService = modelItemType == WSDL_SERVICE ||
                modelItemType == REST_SERVICE;
        return isService;
    }

    public static boolean isMockService(ModelItemType modelItemType) {
        boolean isMockService = modelItemType == WSDL_MOCK_SERVICE || modelItemType == REST_MOCK_SERVICE;
        return isMockService;
    }
}
