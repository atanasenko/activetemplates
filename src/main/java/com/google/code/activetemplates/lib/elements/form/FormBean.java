package com.google.code.activetemplates.lib.elements.form;

import java.util.List;

import javax.xml.stream.events.Attribute;

public class FormBean {

    private String method;
    private String action;
    private Object commandObject;
    private String currentName;
    private String currentValue;
    private List<Attribute> informalAttributes; 

    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    
    public Object getCommandObject() {
        return commandObject;
    }
    public void setCommandObject(Object commandObject) {
        this.commandObject = commandObject;
    }
    
    public String getCurrentName() {
        return currentName;
    }
    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }
    
    public String getCurrentValue() {
        return currentValue;
    }
    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }
    
    public List<Attribute> getInformalAttributes() {
        return informalAttributes;
    }
    public void setInformalAttributes(List<Attribute> informalAttributes) {
        this.informalAttributes = informalAttributes;
    }

}
