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

package com.google.code.activetemplates;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import com.google.code.activetemplates.cache.XmlCache;
import com.google.code.activetemplates.def.TemplateDefinitionSource;
import com.google.code.activetemplates.tiles.TileSource;

public class TemplateBuilderConfig {
    
    private XmlCache xmlCache;
    private Map<String, TileSource> tileSources;
    private List<TemplateDefinitionSource> definitionSources;
    
    public XmlCache getXmlCache() {
        return xmlCache;
    }

    public void setXmlCache(XmlCache xmlCache) {
        this.xmlCache = xmlCache;
    }

    public Map<String, TileSource> getTileSources() {
        return tileSources;
    }

    public void setTileSources(Map<String, TileSource> tileSources) {
        this.tileSources = tileSources;
    }
    
    public void addTileSource(String key, TileSource tileSource) {
        if(tileSources == null) tileSources = new LinkedHashMap<String, TileSource>();
        tileSources.put(key, tileSource);
    }
    
    public List<TemplateDefinitionSource> getDefinitionSources() {
        return definitionSources;
    }

    public void setDefinitionSources(List<TemplateDefinitionSource> definitionSources) {
        this.definitionSources = definitionSources;
    }
    
    public void addDefinitionSource(TemplateDefinitionSource definitionSource) {
        if(definitionSources == null) definitionSources = new ArrayList<TemplateDefinitionSource>();
        definitionSources.add(definitionSource);
    }
}
