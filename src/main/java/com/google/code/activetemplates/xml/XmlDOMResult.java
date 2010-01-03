package com.google.code.activetemplates.xml;

import javax.xml.transform.dom.DOMResult;

public class XmlDOMResult implements XmlResult {

    private DOMResult result;
    
    public XmlDOMResult(DOMResult result) {
        this.result = result;
    }

    @Override
    public void close() {
        
    }

    @Override
    public DOMResult getResult() {
        return result;
    }

}
