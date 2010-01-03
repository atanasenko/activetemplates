package com.google.code.activetemplates.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.events.EventComponent;
import com.google.code.activetemplates.events.TemplateEvent;
import com.google.code.activetemplates.xml.FileXmlCache;
import com.google.code.activetemplates.xml.XmlCache;
import com.google.code.activetemplates.xml.XmlResult;
import com.google.code.activetemplates.xml.XmlSource;

public class EventComponentFactory {
    
    private XmlCache xmlCache;
    private TransformerFactory tFactory;
    
    public EventComponentFactory() {
        xmlCache = new FileXmlCache("temp");

        tFactory = TransformerFactory.newInstance();
    }

    public EventComponent createComponent(TemplateEvent te, Class<? extends ElementHandler> clazz) {
        
        if(!xmlCache.contains(clazz.getName())) {
            InputStream is = clazz.getResourceAsStream(clazz.getSimpleName() + ".xml");
            
            if(is == null) {
                return null;
            }
            
            XmlResult res = xmlCache.createResult(clazz.getName());
            try {
                Transformer t = tFactory.newTransformer();
                t.transform(new StreamSource(is), res.getResult());
            } catch (Exception e) {
                throw new IllegalStateException(e);
            } finally {
                res.close();
                try{ is.close(); } catch(IOException e){}
            }
        }
        
        return new EventComponentImpl(te, new XmlSourceCreator(xmlCache, clazz.getName()));
    }
    
    static class XmlSourceCreator {
        
        XmlCache xmlCache;
        String name;
        
        XmlSourceCreator(XmlCache xmlCache, String name) {
            this.xmlCache = xmlCache;
            this.name = name;
        }
        
        public XmlSource createSource() {
            return xmlCache.createSource(name);
        }
        
    }
}
