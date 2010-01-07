package com.google.code.activetemplates.lib.elements.form;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.events.EndElementEvent;
import com.google.code.activetemplates.events.StartElementEvent;
import com.google.code.activetemplates.impl.handlers.BuiltinHandlerSPI;
import com.google.code.activetemplates.util.TemplateUtils;

public class FormEl implements ElementHandler {

    public static final QName ELEMENT = new QName(
            BuiltinHandlerSPI.NAMESPACE_STDLIB, "form");

    public QName[] getElements() {
        return new QName[]{ ELEMENT };
    }
    public static final QName ATTR_COMMAND_NAME = new QName("commandName");
    
    private static final QName[] FORMAL_PARAMETERS = new QName[]{ 
        FormConstants.ATTR_ACTION, 
        FormConstants.ATTR_METHOD, 
        ATTR_COMMAND_NAME
    };
    
    @Override
    public Outcome processStart(StartElementEvent e) throws XMLStreamException {
        String action = TemplateUtils.getAttribute(e, FormConstants.ATTR_ACTION, "");
        String method = TemplateUtils.getAttribute(e, FormConstants.ATTR_METHOD, "post");
        String command = TemplateUtils.getAttribute(e, ATTR_COMMAND_NAME, "command");
        
        FormBean form = new FormBean();
        form.setAction(action);
        form.setMethod(method);
        form.setCommandObject(e.parseExpression(command, Object.class));
        form.setInformalAttributes(TemplateUtils.getInformalAttributes(e, FORMAL_PARAMETERS));
        
        e.getEnvironment().put(form);
        
        e.setExpressionValue("#form", form);
        return e.getEventComponent().writeComponent();
    }

    @Override
    public void processEnd(EndElementEvent e) throws XMLStreamException {
    }
}
