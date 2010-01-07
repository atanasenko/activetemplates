package com.google.code.activetemplates.lib.elements.form;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.events.EndElementEvent;
import com.google.code.activetemplates.events.StartElementEvent;
import com.google.code.activetemplates.impl.handlers.BuiltinHandlerSPI;
import com.google.code.activetemplates.util.TemplateUtils;

public class TextAreaEl implements ElementHandler {

    public static final QName ELEMENT = new QName(
            BuiltinHandlerSPI.NAMESPACE_STDLIB, "textarea");
    
    private static final QName[] FORMAL_PARAMETERS = new QName[]{ 
        FormConstants.ATTR_PATH, 
        FormConstants.ATTR_NAME
    };
    
    public QName[] getElements() {
        return new QName[]{ ELEMENT };
    }

    @Override
    public Outcome processStart(StartElementEvent e) throws XMLStreamException {
        
        FormBean form = e.getEnvironment().get(FormBean.class);
        String path = TemplateUtils.getAttribute(e, FormConstants.ATTR_PATH);
        
        String data = e.parseExpression(path, form.getCommandObject(), String.class);
        form.setCurrentName(path);
        form.setCurrentValue(data);
        form.setInformalAttributes(TemplateUtils.getInformalAttributes(e, FORMAL_PARAMETERS));
        
        return e.getEventComponent().writeComponent();
    }

    @Override
    public void processEnd(EndElementEvent e) throws XMLStreamException {
    }

}
