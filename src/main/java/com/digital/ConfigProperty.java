package com.digital;

public enum ConfigProperty {
    TIMEOUT_DOM("domTimeout", "30");

    private String propertyName;
    private String defaultValue;

    private ConfigProperty(String propertyName, String defaultValue){
        this.propertyName = propertyName;
        this.defaultValue = defaultValue;
    }

    public String getValue(){
        return System.getProperty(propertyName, defaultValue);
    }

    public int getValueAsInt(){
        return Integer.parseInt(getValue());
    }
}
