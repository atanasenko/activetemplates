package com.google.code.activetemplates.xml;

import javax.xml.transform.dom.DOMSource;

public class XmlDOMSource implements XmlSource {

    private DOMSource source;
    
    public XmlDOMSource(DOMSource source) {
        this.source = source;
    }

    @Override
    public void close() {
        
    }

    @Override
    public DOMSource getSource() {
        return source;
    }

}
