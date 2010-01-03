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

package com.google.code.activetemplates.impl.handlers;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;


import com.google.code.activetemplates.events.AttributeHandler;
import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.spi.HandlerSPI;

public class BuiltinHandlerSPI implements HandlerSPI {
    
    public static final String NAMESPACE_STDLIB = 
        "http://code.google.com/p/activetemplates/ns/stdlib";

    private static final Map<QName, AttributeHandler> attributes = new HashMap<QName, AttributeHandler>();
    private static final Map<QName, ElementHandler> elements = new HashMap<QName, ElementHandler>();
    private static final Set<String> excludedNamespaces = new HashSet<String>();
    
    static {
        
        try {
            addElements("com.google.code.activetemplates.lib.elements");
            addElements("com.google.code.activetemplates.lib.elements.conditional");
            addElements("com.google.code.activetemplates.lib.elements.form");
            addAttributes("com.google.code.activetemplates.lib.attributes");
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
        
        excludedNamespaces.add(NAMESPACE_STDLIB);
        
    }

    public Map<QName, AttributeHandler> getAttributeHandlers() {
        return Collections.unmodifiableMap(attributes);
    }

    public Map<QName, ElementHandler> getElementHandlers() {
        return Collections.unmodifiableMap(elements);
    }

    public Set<String> getExcludedNamespaces() {
        return Collections.unmodifiableSet(excludedNamespaces);
    }
    
    public String getProvider(){
        return "builtin";
    }
    
    private static final void addElements(String pkgName) throws Exception {
        Set<Class<ElementHandler>> classes = getClasses(pkgName, ElementHandler.class);
        for(Class<ElementHandler> cl: classes) {
            ElementHandler eh = cl.newInstance();
            for(QName qn: eh.getElements()) {
                elements.put(qn, eh);
            }
        }
    }

    private static final void addAttributes(String pkgName) throws Exception {
        Set<Class<AttributeHandler>> classes = getClasses(pkgName, AttributeHandler.class);
        for(Class<AttributeHandler> cl: classes) {
            AttributeHandler eh = cl.newInstance();
            for(QName qn: eh.getAttributes()) {
                attributes.put(qn, eh);
            }
        }
    }

    private static final <T> Set<Class<T>> getClasses(String pkg, Class<T> clazz) throws Exception {

        pkg = pkg.replace('.', '/');
        Set<Class<T>> classes = new HashSet<Class<T>>();
        
        ResourcePatternResolver res = new PathMatchingResourcePatternResolver();
        Resource[] resources = res.getResources("classpath*:" + pkg + "/*.class");
        for(Resource r: resources) {
            
            String className = r.getURL().getFile();
            className = className.substring(className.indexOf(pkg), className.length() - ".class".length());
            className = className.replace('/', '.');

            try {
                Class<?> cl = Class.forName(className);
                if(clazz.isAssignableFrom(cl)) {
                    @SuppressWarnings("unchecked")
                    Class<T> tcl = (Class<T>)cl;
                    classes.add(tcl);
                }
            } catch (ClassNotFoundException e) {
            }
        }
        return classes;
        
    }
}
