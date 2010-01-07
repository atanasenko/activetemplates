package com.google.code.activetemplates.events;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import com.google.code.activetemplates.EventStream;

public class EventTemplate {
    
    private QName elementName;
    private List<EventAttribute> attributes;
    
    private EventTemplate(QName elementName) {
        this.elementName = elementName;
    }
    
    public static EventTemplate element(String name) {
        return element(new QName(name));
    }
    
    public static EventTemplate element(QName name) {
        return new EventTemplate(name);
    }
    
    public EventTemplate attribute(String name, String value) {
        return attribute(new QName(name), value);
    }

    public EventTemplate attribute(QName name, String value) {
        if(attributes == null) {
            attributes = new ArrayList<EventAttribute>();
        }
        attributes.add(new EventAttribute(name, value));
        return this;
    }
    
    public void render(EventStream eventStream, NamespaceContext nc) {
        renderStart(eventStream, nc);
        renderEnd(eventStream, nc);
    }
    
    public void renderStart(EventStream eventStream, NamespaceContext nc) {
        String prefix = getPrefix(nc, elementName);
        
        List<Attribute> attrs = new ArrayList<Attribute>();
        
        if(attributes != null) {
            for(EventAttribute ea: attributes) {
                prefix = getPrefix(nc, ea.getName());
                Attribute a = eventStream.getEventFactory().createAttribute(prefix, 
                        ea.getName().getNamespaceURI(), ea.getName().getLocalPart(), ea.getValue());
                attrs.add(a);
            }
        }
        XMLEvent e = eventStream.getEventFactory().createStartElement(prefix, 
                elementName.getNamespaceURI(), elementName.getLocalPart(), attrs.iterator(), null);
        eventStream.queueEvent(e);
    }
    
    public void renderEnd(EventStream eventStream, NamespaceContext nc) {
        String prefix = getPrefix(nc, elementName);
        XMLEvent e = eventStream.getEventFactory().createEndElement(prefix, 
                elementName.getNamespaceURI(), elementName.getLocalPart());
        eventStream.queueEvent(e);
    }
    
    private String getPrefix(NamespaceContext nc, QName name) {
        String namespaceURI = name.getNamespaceURI();
        
        if(namespaceURI == null || namespaceURI.equals(""))
            return "";
        
        String pref = nc.getPrefix(namespaceURI);
        if(pref != null) return pref;
        
        //throw new IllegalStateException("No prefix for " + name + " can be found");
        return "";
        
    }
    
    private static class EventAttribute {
        private QName name;
        private String value;
        public EventAttribute(QName name, String value) {
            this.name = name;
            this.value = value;
        }
        public QName getName() {
            return name;
        }
        public String getValue() {
            return value;
        }
        
    }
}
