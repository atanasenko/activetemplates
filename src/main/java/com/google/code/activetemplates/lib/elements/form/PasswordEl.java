package com.google.code.activetemplates.lib.elements.form;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.events.EndElementEvent;
import com.google.code.activetemplates.events.EventTemplate;
import com.google.code.activetemplates.events.StartElementEvent;
import com.google.code.activetemplates.impl.handlers.BuiltinHandlerSPI;
import com.google.code.activetemplates.lib.attributes.AttributesAt;
import com.google.code.activetemplates.util.TemplateUtils;

public class PasswordEl implements ElementHandler {

    public static final QName ELEMENT = new QName(
            BuiltinHandlerSPI.NAMESPACE_STDLIB, "password");
    
    public static final QName ATTR_SHOW = new QName("showPassword");
    
    private static final QName[] FORMAL_PARAMETERS = new QName[]{ 
        FormConstants.ATTR_PATH, 
        FormConstants.ATTR_NAME, 
        FormConstants.ATTR_VALUE, 
        FormConstants.ATTR_TYPE,
        ATTR_SHOW
    };
    
    private static final EventTemplate TEMPLATE_HID = 
        EventTemplate.element(new QName("input"))
            .attribute(FormConstants.ATTR_TYPE, "password")
            .attribute(FormConstants.ATTR_NAME, "${#form.currentName}")
            .attribute(FormConstants.ATTR_VALUE, "${#form.currentValue}")
            .attribute(AttributesAt.ATTRIBUTE, "#form.informalAttributes");

    private static final EventTemplate TEMPLATE_VIS = 
        EventTemplate.element(new QName("input"))
            .attribute(FormConstants.ATTR_TYPE, "text")
            .attribute(FormConstants.ATTR_NAME, "${#form.currentName}")
            .attribute(FormConstants.ATTR_VALUE, "${#form.currentValue}")
            .attribute(AttributesAt.ATTRIBUTE, "#form.informalAttributes");

    public QName[] getElements() {
        return new QName[]{ ELEMENT };
    }

    @Override
    public Outcome processStart(StartElementEvent e) throws XMLStreamException {
        
        FormBean form = e.getEnvironment().get(FormBean.class);
        String path = TemplateUtils.getAttribute(e, FormConstants.ATTR_PATH);
        boolean show = TemplateUtils.getBooleanAttribute(e, ATTR_SHOW, false);
        
        String data = e.parseExpression(path, form.getCommandObject(), String.class);
        form.setCurrentName(path);
        form.setCurrentValue(data);
        form.setInformalAttributes(TemplateUtils.getInformalAttributes(e, FORMAL_PARAMETERS));
        
        if(show)
            TEMPLATE_VIS.render(e.getEventStream(), e.getEvent().getNamespaceContext());
        else
            TEMPLATE_HID.render(e.getEventStream(), e.getEvent().getNamespaceContext());
            
        
        return null;
    }

    @Override
    public void processEnd(EndElementEvent e) throws XMLStreamException {
    }

}
