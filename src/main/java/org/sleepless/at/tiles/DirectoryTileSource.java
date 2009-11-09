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

package org.sleepless.at.tiles;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

public class DirectoryTileSource implements TileSource {
    
    private File dir;
    
    public DirectoryTileSource(File dir) {
        this.dir = dir;
    }

    @Override
    public Source getTile(String name) {
        return new StreamSource(new File(dir, name));
    }

    @Override
    public boolean readTile(String name, Result res) {
        
        Source s = getTile(name);
        if(s == null) return false;
        
        try {
            
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer t = tFactory.newTransformer();
            t.transform(s, res);
            
        } catch (TransformerConfigurationException e) {
            throw new IllegalStateException(e);
        } catch (TransformerException e) {
            throw new IllegalStateException(e);
        } finally {
            close(s);
        }
        
        return true;
    }

    @Override
    public void close(Source src) {
        StreamSource ss = (StreamSource) src;
        if(ss.getInputStream() != null){
            try{ ss.getInputStream().close(); }
            catch(IOException e){}
        }
    }

}
