package com.google.code.activetemplates.lib.elements.form;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.events.EndElementEvent;
import com.google.code.activetemplates.events.EventTemplate;
import com.google.code.activetemplates.events.StartElementEvent;
import com.google.code.activetemplates.impl.handlers.BuiltinHandlerSPI;
import com.google.code.activetemplates.util.TemplateUtils;

public class HiddenEl implements ElementHandler {

    public static final QName ELEMENT = new QName(
            BuiltinHandlerSPI.NAMESPACE_STDLIB, "hidden");
    
    private static final EventTemplate TEMPLATE = 
        EventTemplate.element(new QName("input"))
            .attribute(FormConstants.ATTR_TYPE, "hidden")
            .attribute(FormConstants.ATTR_NAME, "${#form.currentName}")
            .attribute(FormConstants.ATTR_VALUE, "${#form.currentValue}");

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
        
        TEMPLATE.render(e.getEventStream(), e.getEvent().getNamespaceContext());
        
        return null;
    }

    @Override
    public void processEnd(EndElementEvent e) throws XMLStreamException {
    }

}
