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

import javax.xml.transform.Result;
import javax.xml.transform.Source;

/**
 * This interface is a general contract for caching xml documents.
 * 
 * Note that results and sources returned by this class should be closed explicitly
 * using corresponding close methods defined by this interface.
 * 
 * @author sleepless
 *
 */
public interface XmlCache {

    /**
     * Creates a new result and caches xml document written to it.
     * 
     * @param name
     * @return
     */
    public Result createResult(String name);
    
    /**
     * Returns a cached xml document.
     * 
     * @param name
     * @return
     */
    public Source createSource(String name);
    
    /**
     * Returns true if a document with specified name is cached in this XmlCache
     * @param name
     * @return
     */
    public boolean contains(String name);
    
    /**
     * Closes result returned by createResult method
     * @param res
     */
    public void close(Result res);
    
    /**
     * Closes source returned by createSource method
     * @param src
     */
    public void close(Source src);
    
}
