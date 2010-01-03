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

package com.google.code.activetemplates.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * XmlCache implementation which stores documents as files.
 * 
 * @author sleepless
 *
 */
public class FileXmlCache implements XmlCache {
    
    private File dir;
    
    private Map<String, File> files;

    /**
     * Creates a new FileXmlCache which stores xml documents in temporary folder
     */
    public FileXmlCache() {
        this((File)null);
    }
    
    /**
     * Creates a new FileXmlCache which stores xml documents under specified directory
     * @param file
     */
    public FileXmlCache(String file) {
        this(new File(file));
    }
    
    /**
     * Creates a new FileXmlCache which stores xml documents under specified directory
     * @param f
     */
    public FileXmlCache(File f) {
        files = new HashMap<String, File>();
        this.dir = f;
    }
    
    private File createNewFile(String name) throws IOException {
        
        File f;
        if(dir != null) {
            if(!dir.exists() || !dir.isDirectory()) { 
                if(!dir.mkdirs()) throw new IOException("Cannot create directory " + dir.getAbsolutePath());
            }
            
            f = new File(dir, name);

            if(f.exists()) {
                if(!f.delete()) throw new IOException("File " + f.getAbsolutePath() + " already exists, deletion failed");
            }
            
            File pdir = f.getParentFile();
            if(!pdir.exists() || !pdir.isDirectory()) {
                if(!pdir.mkdirs()) throw new IOException("Cannot create parent directory for " + f.getAbsolutePath());
            }
            //System.out.println("Creating " + f.getAbsolutePath());
            if(!f.createNewFile()) throw new IOException("Cannot create file " + f.getAbsolutePath());
        } else {
            f = File.createTempFile("at-template-", name);
        }
        
        return f;
    }

    @Override
    public XmlResult createResult(String name) {
        try {
            File f = createNewFile(name);
            files.put(name, f);
            
            return new XmlStreamResult(new StreamResult(new FileOutputStream(f)));
        } catch(IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public XmlSource createSource(String name) {
        File f = files.get(name);
        if(f == null) return null;
        return new XmlStreamSource(new StreamSource(f));
    }

    @Override
    public boolean contains(String name) {
        return files.containsKey(name);
    }
    
}
