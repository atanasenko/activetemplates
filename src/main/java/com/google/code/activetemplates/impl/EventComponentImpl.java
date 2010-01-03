package com.google.code.activetemplates.impl;

import java.util.Queue;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.google.code.activetemplates.events.EventComponent;
import com.google.code.activetemplates.events.TemplateEvent;
import com.google.code.activetemplates.events.ElementHandler.Outcome;
import com.google.code.activetemplates.impl.EventComponentFactory.XmlSourceCreator;
import com.google.code.activetemplates.lib.elements.BodyEl;
import com.google.code.activetemplates.util.TemplateUtils;
import com.google.code.activetemplates.xml.XmlSource;

class EventComponentImpl implements EventComponent {
    
    private TemplateEvent e;
    private XmlSourceCreator componentSourceCreator;
    
    public EventComponentImpl(TemplateEvent e, XmlSourceCreator componentSourceCreator) {
        this.e = e;
        this.componentSourceCreator = componentSourceCreator;
    }

    @Override
    public Outcome writeComponent() throws XMLStreamException {
        
        XmlSource componentSource = componentSourceCreator.createSource();
        XMLEventReader r = null;
        
        try {
            Queue<XMLEvent> body = TemplateUtils.readChildren(e.getEventStream(), false);
            BodyEl.setBody(e, body);

            r = XMLInputFactory.newInstance().createXMLEventReader(componentSource.getSource());
            while(r.hasNext()) {
                e.getEventStream().queueEvent(r.nextEvent());
            }
            
        } finally {
            componentSource.close();
            if(r != null) r.close();
        }
        return Outcome.PROCESS_CHILDREN;
    }

}
