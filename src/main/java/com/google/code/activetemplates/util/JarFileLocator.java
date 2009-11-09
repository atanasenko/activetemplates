package com.google.code.activetemplates.util;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;

public class JarFileLocator {
    
    public static List<JarFile> findJarFiles(JarFileFilter f) {
        
        Enumeration<URL> en;
        
        try {
            en = JarFileLocator.class.getClassLoader().getResources("/META-INF");
        
            List<JarFile> jarFiles = new ArrayList<JarFile>();
            
            while(en.hasMoreElements()) {
                URL u = en.nextElement();
                
                URLConnection c = u.openConnection();
                if(c instanceof JarURLConnection) {
                    
                    JarURLConnection jc = (JarURLConnection) c;
                    JarFile jf = jc.getJarFile();
                    if(f.accept(jf)) {
                        jarFiles.add(jf);
                    }
                }
            }
        
            return jarFiles;
            
        } catch(IOException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public static List<JarFile> findJarFiles(final String pattern) {
        return findJarFiles(new JarFileFilter() {
            public boolean accept(JarFile jf) {
                return jf.getName().matches(pattern);
            }
        });
    }

    
    public interface JarFileFilter {
        
        public boolean accept(JarFile jf);
        
    }
}
