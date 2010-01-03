package com.google.code.activetemplates.xml;

import java.io.IOException;

import javax.xml.transform.stream.StreamSource;

public class XmlStreamSource implements XmlSource {

    private StreamSource source;
    
    public XmlStreamSource(StreamSource source) {
        this.source = source;
    }

    @Override
    public void close() {
        if(source.getInputStream() != null){
            try{ source.getInputStream().close(); }
            catch(IOException e){}
        }
    }

    @Override
    public StreamSource getSource() {
        return source;
    }

}
