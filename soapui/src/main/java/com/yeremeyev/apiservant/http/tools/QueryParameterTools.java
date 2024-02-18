package com.yeremeyev.apiservant.http.tools;

public class QueryParameterTools {

    /**
     * convert to valid query view
     */
    public static String encode(String value) {
        String result = value;

        result = result.replaceAll(" ", "%20");
        result = result.replaceAll("\n", "%0A");
        result = result.replaceAll("\r", "%0D");

        return result;
    }

    /**
     * @param value raw parameter
     * @return readable view
     */
    public static String decode(String value) {
        String result = value;

        result = result.replaceAll("%0D", "\r");
        result = result.replaceAll("%0A", "\n");
        result = result.replaceAll("%20", " ");

        return result;
    }
}
