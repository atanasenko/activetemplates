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

package com.google.code.activetemplates.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;

/**
 * XmlCache implementation which stores xml documents in memory
 * @author sleepless
 *
 */
public class MemoryXmlCache implements XmlCache {
    
    private DocumentBuilder dBuilder;
    private Map<String, Document> documents;
    
    /**
     * Creates a new MemoryXmlCache
     */
    public MemoryXmlCache(){
        documents = new HashMap<String, Document>();
        try {
            dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public XmlResult createResult(String name) {
        Document doc = dBuilder.newDocument();
        documents.put(name, doc);
        return new XmlDOMResult(new DOMResult(doc));
    }

    @Override
    public XmlSource createSource(String name) {
        Document doc = documents.get(name);
        if(doc == null) return null;
        return new XmlDOMSource(new DOMSource(doc));
    }

    @Override
    public boolean contains(String name) {
        return documents.containsKey(name);
    }


}
