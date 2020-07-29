package com.eviware.soapui.plugins.interfaces.controls;

/**
 *  Enumerated extendable ReadyAPI trees.
 *  For first plugin version only navigator is supported
 *  
 */
public enum TreeType {

    MAIN_NAVIGATOR(1, "Main Navigator Tree", "Main Navigator Tree");

    private int id;
    private String name;
    private String description;

    private TreeType(int id, String name, String description) {
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
