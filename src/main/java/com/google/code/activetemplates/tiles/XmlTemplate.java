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

package com.google.code.activetemplates.tiles;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class XmlTemplate {
    
    private String name;
    private String source;
    private String superTemplate;
    private boolean empty;
    private boolean isAbstract;
    private String transformation;
    private List<XmlTemplate> includes;
    
    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    @XmlAttribute(name = "src")
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    
    @XmlAttribute(name = "extends")
    public String getSuperTemplate() {
        return superTemplate;
    }
    public void setSuperTemplate(String superTemplate) {
        this.superTemplate = superTemplate;
    }
    
    @XmlAttribute(name = "empty")
    public boolean isEmpty() {
        return empty;
    }
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
    
    @XmlAttribute(name = "abstract")
    public boolean isAbstract() {
        return isAbstract;
    }
    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }
    
    @XmlAttribute(name = "transform")
    public String getTransformation() {
        return transformation;
    }
    public void setTransformation(String transformation) {
        this.transformation = transformation;
    }
    
    @XmlElement(name = "include")
    public List<XmlTemplate> getIncludes() {
        return includes;
    }
    public void setIncludes(List<XmlTemplate> includes) {
        this.includes = includes;
    }
    
}
