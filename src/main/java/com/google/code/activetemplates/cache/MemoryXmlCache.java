/*
 * Copyright 2009 Anton Tanasenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.code.activetemplates.cache;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;

public class MemoryXmlCache implements XmlCache {
    
    private DocumentBuilder dBuilder;
    private Map<String, Document> documents;
    
    public MemoryXmlCache(){
        documents = new HashMap<String, Document>();
        try {
            dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Result createResult(String name) {
        Document doc = dBuilder.newDocument();
        documents.put(name, doc);
        return new DOMResult(doc);
    }

    @Override
    public Source getSource(String name) {
        Document doc = documents.get(name);
        if(doc == null) return null;
        return new DOMSource(doc);
    }

    @Override
    public void close(Result res) {
    }

    @Override
    public void close(Source src) {
    }
    
    @Override
    public boolean contains(String name) {
        return documents.containsKey(name);
    }


}
