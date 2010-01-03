package com.google.code.activetemplates.lib.attributes;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;

import com.google.code.activetemplates.events.AttributeEvent;
import com.google.code.activetemplates.events.AttributeHandler;
import com.google.code.activetemplates.impl.handlers.BuiltinHandlerSPI;

public class AttributesAt implements AttributeHandler {
    
    public static final QName ATTRIBUTE = new QName(
            BuiltinHandlerSPI.NAMESPACE_STDLIB, "attributes");

    public QName[] getAttributes() {
        return new QName[]{ ATTRIBUTE };
    }

    public Outcome processAttribute(AttributeEvent e) {

        Attribute a = e.getEvent();
        String path = a.getValue();
        
        @SuppressWarnings("unchecked")
        List<Attribute> attrs = e.parseExpression(path, List.class);
        
        for(Attribute attr: attrs) {
            e.getEventStream().queueEvent(attr);
        }
        
        return null;
    }
}
