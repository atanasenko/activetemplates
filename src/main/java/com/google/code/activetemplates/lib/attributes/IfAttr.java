package com.google.code.activetemplates.lib.attributes;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;

import com.google.code.activetemplates.events.AttributeEvent;
import com.google.code.activetemplates.events.AttributeHandler;
import com.google.code.activetemplates.impl.handlers.StandardHandlerSPI;

public class IfAttr implements AttributeHandler {

    public static final QName ATTRIBUTE = new QName(StandardHandlerSPI.NAMESPACE_STDLIB, "if");

    public Outcome processAttribute(AttributeEvent e) {
        
        Attribute a = e.getEvent();
        String v = a.getValue();
        
        if(v == null) {
            throw new IllegalStateException("Condition not specified");
        }
        
        if(!e.getScriptingProvider().evalBoolean(v, e.getBindings())) {
            return Outcome.PROCESS_NONE;
        }
        
        return null;
    }

}
