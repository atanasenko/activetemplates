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
