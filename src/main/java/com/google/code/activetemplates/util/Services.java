package com.google.code.activetemplates.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.spi.ServiceRegistry;

public class Services {
    
    public static <T> List<T> getProviders(Class<T> cl) {
        List<T> l = new ArrayList<T>();
        Iterator<T> it = ServiceRegistry.lookupProviders(cl);
        while(it.hasNext()) l.add(it.next());
        return l;
    }
    
}
