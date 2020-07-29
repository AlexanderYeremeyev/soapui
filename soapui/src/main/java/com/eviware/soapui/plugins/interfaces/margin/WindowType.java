package com.eviware.soapui.plugins.interfaces.margin;

/**
 * Enumerated extendable SoapUI OS windows.
 * For first version only navigator is supported
 */
public enum WindowType {
    MAIN_NAVIGATOR(1, "Main Navigator Panel", "Main Navigator Panel. Window at the left part of window. Navigator shows project trees.");

    private int id;
    private String name;
    private String description;

    private WindowType(int id, String name, String description) {
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
}
