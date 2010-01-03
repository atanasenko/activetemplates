package com.google.code.activetemplates.lib.elements.form;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;

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

    public static final QName ATTR_ACTION = new QName("action");
    public static final QName ATTR_METHOD = new QName("method");
    
    @Override
    public Outcome processStart(StartElementEvent e) throws XMLStreamException {
        String action = "";
        String method = "post";
        Attribute aAction = e.getEvent().getAttributeByName(ATTR_ACTION);
        Attribute aMethod = e.getEvent().getAttributeByName(ATTR_METHOD);
        if(aAction != null) action = aAction.getValue();
        if(aMethod != null) method = aMethod.getValue();
        
        FormBean fb = new FormBean();
        fb.setAction(action);
        fb.setMethod(method);
        fb.setInformalAttributes(TemplateUtils.getInformalAttributes(e, ATTR_ACTION, ATTR_METHOD));
        
        e.setExpressionValue("#form", fb);
        return e.getEventComponent().writeComponent();
    }

    @Override
    public Outcome processEnd(EndElementEvent e) throws XMLStreamException {
        return null;
    }

    public static class FormBean {
        
        private String method;
        private String action;
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
        
        public List<Attribute> getInformalAttributes() {
            return informalAttributes;
        }
        public void setInformalAttributes(List<Attribute> informalAttributes) {
            this.informalAttributes = informalAttributes;
        }
        
    }
}
