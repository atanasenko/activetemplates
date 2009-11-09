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

package org.sleepless.at.def;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

public class XmlTemplateDefinitionSource implements TemplateDefinitionSource {
    
    private Source src;
    private JAXBContext jaxb;

    public XmlTemplateDefinitionSource(Source src) {
        this.src = src;
        try {
            jaxb = JAXBContext.newInstance(XmlTemplateDefinitionConfig.class);
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
        
    }
    
    public XmlTemplateDefinitionSource(File f) {
        this(new StreamSource(f));
    }

    public XmlTemplateDefinitionSource(InputStream is) {
        this(new StreamSource(is));
    }

    @Override
    public List<TemplateDefinition> getDefinitions() {
        
        XmlTemplateDefinitionConfig c;
        try {
            Unmarshaller um = jaxb.createUnmarshaller();
            c = (XmlTemplateDefinitionConfig) um.unmarshal(src);
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
        
        // create flat definitions from template tree
        List<TemplateDefinition> l = new ArrayList<TemplateDefinition>();
        createDefinitions(null, c.getTemplates(), l);
        
        return l;
    }

    private void createDefinitions(TemplateDefinition parent, List<XmlTemplate> templates, List<TemplateDefinition> l) {
        if(templates == null) return;
        
        for(XmlTemplate xt: templates) {
            
            String name = xt.getName();
            if(name == null) {
                throw new IllegalArgumentException("Template name must be set");
            }
            TemplateDefinition td = new TemplateDefinition();

            // set properties
            if(parent != null) {
                td.setName(parent.getName() + "/" + name);
                parent.getInclusions().put(name, td.getName());
                
            } else {
                td.setName(name);
            }
            
            td.setInternal(parent != null);
            td.setAbstract(!td.isInternal() && xt.isAbstract());
            td.setEmpty(xt.isEmpty());
            td.setSuperTemplate(xt.getSuperTemplate());
            td.setSource(xt.getSource());
            td.setTransformation(xt.getTransformation());
            td.setInclusions(new LinkedHashMap<String, String>());
            
            l.add(td);
            
            // walk recursively
            createDefinitions(td, xt.getIncludes(), l);
        }
        
    }
    
    
}
