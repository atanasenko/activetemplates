package com.google.code.activetemplates.xml;

import javax.xml.transform.Source;

public interface XmlSource {
    
    public Source getSource();
    
    public void close();
    
}
