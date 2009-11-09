package com.google.code.activetemplates.util.deps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Simple to use dependency chaining mechanism.
 * The task of ordering dependency nodes can be simplified by assigning weight
 * to each node and sorting them. Node weight is calculated as following:
 * weight = 1 + sum(dep.weight)
 * 
 * @author sleepless
 *
 * @param <K>
 */
public class DependencyTree<K extends DependencyNode> {

    private Map<String, K> depMap;
    
    public DependencyTree() {
        depMap = new HashMap<String, K>();
    }
    
    
    /**
     * Add a new dependency node.
     * 
     * @param node
     * @throws IllegalArgumentException, if node dependencies cannot be resolved
     */
    public void add(K node) {
        
        // cyclic dependencies are impossible, since no node with 
        // non-existent dependencies can be added
        
        Set<String> deps = node.getDependencies();
        if(deps != null) {
            for(String d: deps) {
                if(!depMap.containsKey(d)) {
                    throw new IllegalArgumentException(String.format("Cannot resolve %s -> %s", node.getId(), d));
                }
            }
        }
        depMap.put(node.getId(), node);
    }
    
    /**
     * Adds several nodes to this tree.
     * Nodes passed to this method are not required to be sorted by their dependencies
     * (e.g. it could contain [a(b),b()], but adding them sequentially with add() method
     * would fail)
     * 
     * @param nodes
     */
    public void addAll(Collection<? extends K> nodes) {
        // sort new nodes so that they can be added serially
        for(WeighedNode node: sortDeps(nodes)) {
            add(node.k);
        }
    }
    
    /**
     * Retrieve chain of processing, node, specified by the id parameter
     * will be at the end of chain.
     * @param id
     * @return
     */
    public DependencyChain<K> getChain(String id) {
        
        // collect and sort dependency tree
        List<K> nodes = new ArrayList<K>();
        collectDeps(id, nodes);
        List<WeighedNode> wn = sortDeps(nodes);
        
        // build chain
        Collections.reverse(wn);
        DependencyChain<K> c = null;
        for(WeighedNode w: wn) {
            c = new DependencyChain<K>(w.k, c);
        }
        
        return c;
    }
    
    /**
     * Retrieves chain of processing, which contains all of the nodes of this tree.
     * 
     * @return
     */
    public DependencyChain<K> getRootChain(){

        List<K> nodes = new ArrayList<K>(depMap.values());
        List<WeighedNode> wn = sortDeps(nodes);
        
        // build chain
        Collections.reverse(wn);
        DependencyChain<K> c = null;
        for(WeighedNode w: wn) {
            c = new DependencyChain<K>(w.k, c);
        }
        
        return c;
    }
    
    
    private void collectDeps(String name, List<K> nodes) {
        
        // collect dependency tree recursively
        K k = depMap.get(name);
        if(k == null) throw new IllegalArgumentException(String.format("No such node: %s", name));
        nodes.add(k);
        Set<String> deps = k.getDependencies();
        if(deps != null) {
            for(String d: deps) {
                collectDeps(d, nodes);
            }
        }
    }
    
    private List<WeighedNode> sortDeps(Collection<? extends K> nodes) {
        
        // index nodes
        Map<String, WeighedNode> m = new HashMap<String, WeighedNode>();
        for(K k: nodes) {
            m.put(k.getId(), new WeighedNode(k));
        }
        
        // resolve dependencies
        for(WeighedNode w: m.values()) {
            Set<String> deps = w.k.getDependencies();
            if(deps != null) {
                for(String d: deps) {
                    // resolve only those present in index
                    if(m.containsKey(d)) {
                        w.addDep(m.get(d));
                    }
                }
            }
        }
        
        // sort elements by their weight
        List<WeighedNode> wn = new ArrayList<WeighedNode>();
        wn.addAll(m.values());
        Collections.sort(wn);
        
        return wn;
    }
    
    private class WeighedNode implements Comparable<WeighedNode> {
        
        K k;
        List<WeighedNode> deps;
        
        WeighedNode(K k) {
            this.k = k;
        }
        
        void addDep(WeighedNode wn) {
            if(deps == null) deps = new ArrayList<WeighedNode>();
            deps.add(wn);
        }
        
        @Override
        public int compareTo(WeighedNode o) {
            return getWeight() - o.getWeight();
        }
        
        int getWeight(){
            int w = 1;
            if(deps != null) {
                for(WeighedNode wn: deps) {
                    w += wn.getWeight();
                }
            }
            return w;
        }
        
    }
    
}
