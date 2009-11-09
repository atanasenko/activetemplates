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

import java.util.Map;

public class TemplateDefinition {
    
    public static final TemplateDefinition EMPTY = 
        new TemplateDefinition(){{ setName("empty"); setEmpty(true); }};

    private boolean internal;

    private String name;
    private String superTemplate;
    private boolean empty;
    private String source;

    private boolean isAbstract;
    private Map<String, String> inclusions;
    
    private String transformation;
    
    public boolean isInternal() {
        return internal;
    }
    public void setInternal(boolean internal) {
        this.internal = internal;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSuperTemplate() {
        return superTemplate;
    }
    public void setSuperTemplate(String superTemplate) {
        this.superTemplate = superTemplate;
    }
    
    public boolean isEmpty() {
        return empty;
    }
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
    
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    
    public boolean isAbstract() {
        return isAbstract;
    }
    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }
    
    public Map<String, String> getInclusions() {
        return inclusions;
    }
    public void setInclusions(Map<String, String> inclusions) {
        this.inclusions = inclusions;
    }
    
    public String getTransformation() {
        return transformation;
    }
    public void setTransformation(String transformation) {
        this.transformation = transformation;
    }
    
}
