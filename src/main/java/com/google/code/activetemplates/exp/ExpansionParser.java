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

package com.google.code.activetemplates.exp;

import java.util.HashMap;
import java.util.Map;

public class ExpansionParser {

    private static final Map<Character, String> escapes = new HashMap<Character, String>();
    
    static {
        escapes.put('$', "$");
        escapes.put('\\', "\\");
    }

    public static Expansion parse(String s){
        
        ExpansionParser p = new ExpansionParser(s);
        return p.parse(false);
    }
    
    private int idx;
    private String source;
    private StringBuilder sb;
    
    private ExpansionParser(String source) {
        this.source = source;
        idx = 0;
        sb = new StringBuilder();
    }
    
    private CompoundExpansion parse(boolean inner){
        
        CompoundExpansion ce = new CompoundExpansion();
        
        int idxStart = idx;
        
        while(true) {
            
            if(idx >= source.length()) {
                // end of stream
                
                if(inner) {
                    // unexpected end of inner expression
                    throw new IllegalStateException("Unexpected end of stream reached at `" + source.substring(idxStart, idx) + "`");
                } else {
                    // end of outer stream
                    appendString(ce);
                    break;
                }
                
            } else if(source.charAt(idx) == '\\') {
                // escape - next char will be escaped or go in as-is
                
                idx++;
                char c = source.charAt(idx);
                //System.out.println("Escape char, next = `" + c + "`");
                if(escapes.containsKey(c)) {
                    sb.append(escapes.get(c));
                } else {
                    sb.append(c);
                }
                idx++;
                
            } else if(source.charAt(idx) == '$' && source.charAt(idx+1) == '{') {
                // start of inner expansion
                
                appendString(ce);
                
                idx += 2;
                ce.addExpansion(new BindingExpansion(parse(true)));
                
            } else if(source.charAt(idx) == '}' && inner) {
                // end of inner expansion
                
                appendString(ce);
                idx++;
                break;
            
            } else {
                // text input
                
                sb.append(source.charAt(idx));
                idx++;
                
            }
        }
        
        return ce;
    }
    
    private void appendString(CompoundExpansion ce) {
        if(sb.length() > 0) {
            ce.addExpansion(new StringExpansion(sb.toString()));
            sb.setLength(0);
        }
    }
    
}
