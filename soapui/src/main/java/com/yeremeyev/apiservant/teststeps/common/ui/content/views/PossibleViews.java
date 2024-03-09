package com.yeremeyev.apiservant.teststeps.common.ui.content.views;

public enum PossibleViews {
    RAW("raw", "simple readonly view"),
    TEXT("message", "simple text editor"),
    JSON("json", "json view");

    private String name;
    private String description;

    private PossibleViews(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
