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

package com.google.code.activetemplates.bind;

/**
 * Bindings is a map-like container for key-value pairs in use by template compilers
 * and scripting providers. Usually they are shared among those.
 * 
 * The difference from the more common map is that Bindings are hierarchical:
 * bind and unbind operations set and remove bindings in the current object only, 
 * while lookup operation searches up the hierarchy if the corresponding key cannot
 * be found in the current context.
 * 
 * @author sleepless
 *
 */
public interface Bindings {

    /**
     * Binds a new key-value pair in the current context
     * 
     * @param name
     * @param value
     */
    public void bind(String name, Object value);

    /**
     * Unbinds (removes) a key-value pair from the current context
     * 
     * @param name
     */
    public void unbind(String name);

    /**
     * Searches binding hierarchy for the specified value
     * @param name
     * @return
     */
    public Object lookup(String name);

    /**
     * Searches binding hierarchy for the first occurence of any object
     * which extends or implements specified class/interface.
     * @param <K>
     * @param cl
     * @return
     */
    public <K> K lookup(Class<K> cl);

    /**
     * Searches binding hierarchy for the specified value with a specified class.
     * @param <K>
     * @param cl
     * @param name
     * @return
     */
    public <K> K lookup(Class<K> cl, String name);
    
    /**
     * Returns parent bindings
     * 
     * @return
     */
    public Bindings getParent();

}