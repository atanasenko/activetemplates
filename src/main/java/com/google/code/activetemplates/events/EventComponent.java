package com.google.code.activetemplates.events;

import javax.xml.stream.XMLStreamException;

import com.google.code.activetemplates.events.ElementHandler.Outcome;

public interface EventComponent {
    
    public Outcome writeComponent() throws XMLStreamException;
    
}
