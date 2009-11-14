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

public class ExpansionParser {

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
                
            } else if(isStart()) {
                // start of inner expansion
                
                appendString(ce);
                
                idx += 2;
                ce.addExpansion(new BindingExpansion(parse(true)));
                
            } else if(isEnd()) {
                // end of inner expansion
                
                if(!inner) {
                    throw new IllegalStateException("Unexpected '}' encountered");
                }
                
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
    
    private boolean isStart(){
        if(source.charAt(idx) == '$' && source.charAt(idx+1) == '{') {
            return !isEscaped();
        }
        return false;
    }
    
    private boolean isEnd(){
        if(source.charAt(idx) == '}') {
            return !isEscaped();
        }
        return false;
    }
    
    private boolean isEscaped(){
        // check for escapes
        if(idx > 1 && source.charAt(idx-1) == '\\' && source.charAt(idx-2) == '\\') {
            // double escape: convert to one escape
            sb.setLength(sb.length() - 1); // strips one slash from the end
        } else if(idx > 0 && source.charAt(idx-1) == '\\') {
            // single slash: not a start
            sb.setLength(sb.length() - 1); // strips this slash from the end
            return true;
        }
        return false;
    }
    
    private void appendString(CompoundExpansion ce) {
        if(sb.length() > 0) {
            ce.addExpansion(new StringExpansion(sb.toString()));
            sb.setLength(0);
        }
    }
    
}
