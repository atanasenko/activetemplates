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

package org.sleepless.at.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.sleepless.at.Template;
import org.sleepless.at.TemplateBuilder;
import org.sleepless.at.TemplateBuilderConfig;
import org.sleepless.at.Template.Access;
import org.sleepless.at.cache.MemoryXmlCache;
import org.sleepless.at.cache.XmlCache;
import org.sleepless.at.def.TemplateDefinition;
import org.sleepless.at.def.TemplateDefinitionSource;
import org.sleepless.at.tiles.TileSource;
import org.sleepless.core.deps.DependencyNode;
import org.sleepless.core.deps.DependencyTree;

public class TemplateBuilderImpl implements TemplateBuilder {

    private XmlCache xmlCache;
    private Map<String, TileSource> tileSources;
    private List<TemplateDefinitionSource> definitionSources;
    
    public TemplateBuilderImpl(TemplateBuilderConfig config) {
        
        xmlCache = config.getXmlCache();
        
        if(xmlCache == null) {
            xmlCache = new MemoryXmlCache();
        }
        
        tileSources = config.getTileSources();
        definitionSources = config.getDefinitionSources();
    }
    
    @Override
    public List<Template> build() {
        
        List<TemplateNode> tnodes = new ArrayList<TemplateNode>();
        
        for(TemplateDefinitionSource ds: definitionSources) {
            
            for(TemplateDefinition def: ds.getDefinitions()){
                tnodes.add(new TemplateNode(def));
            }
            
        }
        
        DependencyTree<TemplateNode> dt = new DependencyTree<TemplateNode>();
        dt.addAll(tnodes);
        
        Map<String, TemplateImpl> templates = new LinkedHashMap<String, TemplateImpl>();
        
        List<Template> l = new ArrayList<Template>();
        for(TemplateNode tn: dt.getRootChain()) {
            TemplateDefinition def = tn.getDefinition();
            
            TemplateImpl t = buildTemplate(def, templates);
            templates.put(t.getName(), t);
            
            if(t.getAccess() == Access.CONCRETE) {
                l.add(t);
            }
        }
        
        return l;
    }
    
    private TemplateImpl buildTemplate(TemplateDefinition td, Map<String, TemplateImpl> templates) {
        
        TemplateImpl t = new TemplateImpl(xmlCache);
        t.setName(td.getName());
        if(td.isInternal()) {
            t.setAccess(Access.INTERNAL);
        } else if(td.isAbstract()) {
            t.setAccess(Access.ABSTRACT);
        } else {
            t.setAccess(Access.CONCRETE);
        }

        if(!td.isEmpty()) {
            t.setInclusions(new HashMap<String, String>());
            if(td.getSuperTemplate() != null) {
                // inherit source name and inclusions from parent template
                TemplateImpl st = templates.get(td.getSuperTemplate());
                t.setSourceName(st.getSourceName());
                t.getInclusions().putAll(st.getInclusions());
            } else if(td.isEmpty()) {
                
                t.setSourceName(null);
                
            } else {
                t.setSourceName(td.getSource().replaceAll(":", "-"));

                // read tile source if not done already
                if(!t.hasRawSource()) {
                    Result res = t.createRawResult();
                    try {
                        readTile(td.getSource(), res);
                    } finally {
                        t.getXmlCache().close(res);
                    }
                }
                
            }
            
            // override inclusions with those in definition
            t.getInclusions().putAll(td.getInclusions());
            
            // merge source with inclusions
            if(t.getAccess() != Access.ABSTRACT) {
                Map<String, Source> incSources = new HashMap<String, Source>();
                Source s = t.getRawSource();
                Result r = t.createResult();
                try {
                    
                    for(Map.Entry<String, String> e: t.getInclusions().entrySet()) {
                        TemplateImpl incTemplate = templates.get(e.getValue());
                        incSources.put(e.getKey(), incTemplate.getSource());
                    }
                    new TemplateMerger(t.getName(), r, s, incSources).merge();
                    
                } catch (XMLStreamException xe) {
                    
                    throw new IllegalStateException(xe);
                    
                } finally {
                    
                    t.getXmlCache().close(s);
                    t.getXmlCache().close(r);
                    for(Map.Entry<String, Source> e: incSources.entrySet()) {
                        if(e.getValue() != null) {
                            TemplateImpl incTemplate = templates.get(t.getInclusions().get(e.getKey()));
                            if(incTemplate == null) throw new IllegalStateException("Template " + e.getKey() + " not found in the cache");
                            incTemplate.getXmlCache().close(e.getValue());
                        }
                    }
                    
                }
            }
            
        }
        
        return t;
    }
    
    
    private void readTile(String name, Result res) {
        
        String[] sn = name.split(":", 2);
        if(sn.length != 2) throw new IllegalArgumentException("Tile named " + name + " does not contain tileSource name");
        
        TileSource ts = tileSources.get(sn[0]);
        if(ts == null) throw new IllegalArgumentException("No such tileSource: " + sn[0]);
        
        if(!ts.readTile(sn[1], res)) throw new IllegalArgumentException("No such tile: " + name);
    }
    
    private static class TemplateNode implements DependencyNode {
        
        private TemplateDefinition definition;
        
        public TemplateNode(TemplateDefinition definition) {
            this.definition = definition;
        }
        
        public TemplateDefinition getDefinition(){
            return definition;
        }

        public Set<String> getDependencies() {
            Set<String> s = new HashSet<String>();
            if(definition.getSuperTemplate() != null) {
                s.add(definition.getSuperTemplate());
            }
            s.addAll(definition.getInclusions().values());
            
            return s;
        }

        public String getId() {
            return definition.getName();
        }
        
    }

}
