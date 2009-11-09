package com.google.code.activetemplates.util.deps;

import java.util.Iterator;

/**
 * Chain of dependency nodes which can be processed in order of dependencies.
 * 
 * @author sleepless
 *
 * @param <K>
 */
public class DependencyChain <K extends DependencyNode> implements Iterable<K> {

    private DependencyChain<K> next;
    private K node;
    
    public DependencyChain(K node, DependencyChain<K> next) {
        this.node = node;
        this.next = next;
    }
    
    /**
     * Get next dependency chain element
     * @return
     */
    public DependencyChain<K> next(){
        return next;
    }
    
    /**
     * Get current chain element node 
     * @return
     */
    public K getNode(){
        return node;
    }

    /**
     * Returns this chain as an Iterator of its nodes
     */
    public Iterator<K> iterator() {
        return new ChainIterator();
    }
    
    private class ChainIterator implements Iterator<K> {
        
        private DependencyChain<K> current = DependencyChain.this;

        public boolean hasNext() {
            return current != null;
        }

        public K next() {
            
            try {
                return current.getNode();
            } finally {
                current = current.next();
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
}
