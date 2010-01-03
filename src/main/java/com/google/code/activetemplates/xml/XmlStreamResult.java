package com.google.code.activetemplates.xml;

import java.io.IOException;

import javax.xml.transform.stream.StreamResult;

public class XmlStreamResult implements XmlResult {

    private StreamResult result;
    
    public XmlStreamResult(StreamResult result) {
        this.result = result;
    }
    
    @Override
    public void close() {
        if(result.getOutputStream() != null){
            try{ result.getOutputStream().close(); }
            catch(IOException e){}
        }        
    }

    @Override
    public StreamResult getResult() {
        return result;
    }

}
