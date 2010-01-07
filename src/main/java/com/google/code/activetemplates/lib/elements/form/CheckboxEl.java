package com.google.code.activetemplates.lib.elements.form;

import java.util.Collection;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.events.EndElementEvent;
import com.google.code.activetemplates.events.EventTemplate;
import com.google.code.activetemplates.events.StartElementEvent;
import com.google.code.activetemplates.impl.handlers.BuiltinHandlerSPI;
import com.google.code.activetemplates.lib.attributes.AttributesAt;
import com.google.code.activetemplates.util.TemplateUtils;

public class CheckboxEl implements ElementHandler {

    public static final QName ELEMENT = new QName(
            BuiltinHandlerSPI.NAMESPACE_STDLIB, "checkbox");
    
    private static final QName[] FORMAL_PARAMETERS = new QName[]{ 
        FormConstants.ATTR_PATH, 
        FormConstants.ATTR_NAME,
        FormConstants.ATTR_VALUE, 
        FormConstants.ATTR_TYPE, 
        FormConstants.ATTR_CHECKED
    };
    
    private static final EventTemplate TEMPLATE1 = 
        EventTemplate.element(new QName("input"))
            .attribute(FormConstants.ATTR_TYPE, "checkbox")
            .attribute(FormConstants.ATTR_NAME, "${#form.currentName}")
            .attribute(FormConstants.ATTR_VALUE, "${#form.currentValue}")
            .attribute(AttributesAt.ATTRIBUTE, "#form.informalAttributes");

    private static final EventTemplate TEMPLATE2 = 
        EventTemplate.element(new QName("input"))
            .attribute(FormConstants.ATTR_TYPE, "hidden")
            .attribute(FormConstants.ATTR_NAME, "_${#form.currentName}")
            .attribute(FormConstants.ATTR_VALUE, "1");

    public QName[] getElements() {
        return new QName[]{ ELEMENT };
    }

    @Override
    public Outcome processStart(StartElementEvent e) throws XMLStreamException {
        
        FormBean form = e.getEnvironment().get(FormBean.class);
        String path = TemplateUtils.getAttribute(e, FormConstants.ATTR_PATH);
        String value = e.parseTemplateExpression(
                TemplateUtils.getAttribute(e, FormConstants.ATTR_VALUE, ""), String.class);
        
        Object data = e.parseExpression(path, form.getCommandObject(), Object.class);
        boolean checked;
        if(data == null) {
            checked = false;
        } else if(data instanceof Boolean) {
            checked = ((Boolean)data).booleanValue();
        } else if(data instanceof Collection<?>) {
            Collection<?> coll = (Collection<?>) data;
            checked = false;
            for(Object o: coll) {
                if(o != null && o.toString().equals(value)) {
                    checked = true;
                    break;
                }
            }
        } else {
            checked = data.toString().equals(value);
        }
        form.setCurrentName(path);
        form.setCurrentValue(value);
        form.setInformalAttributes(TemplateUtils.getInformalAttributes(e, FORMAL_PARAMETERS));
        
        TEMPLATE1.renderStart(e.getEventStream(), e.getEvent().getNamespaceContext());
        if(checked) {
            e.getEventStream().queueEvent(
                    e.getEventStream().getEventFactory()
                    .createAttribute(FormConstants.ATTR_CHECKED, "checked"));
        }
        TEMPLATE1.renderEnd(e.getEventStream(), e.getEvent().getNamespaceContext());
        TEMPLATE2.render(e.getEventStream(), e.getEvent().getNamespaceContext());
        
        return null;
    }

    @Override
    public void processEnd(EndElementEvent e) throws XMLStreamException {
    }


}
