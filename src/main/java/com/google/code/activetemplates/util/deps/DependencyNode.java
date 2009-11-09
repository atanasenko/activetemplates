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

package com.google.code.activetemplates.util.deps;

import java.util.Set;

/**
 * Defines contract for dependency mechanism.
 * Each node (some service for example) can be marked dependent on some other nodes,
 * defining the order, in which dependency chain must be processed.
 * 
 * @author sleepless
 *
 */
public interface DependencyNode {

    String getId();
    
    Set<String> getDependencies();
    
}
