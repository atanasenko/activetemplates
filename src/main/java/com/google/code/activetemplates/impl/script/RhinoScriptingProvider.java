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

package com.google.code.activetemplates.impl.script;

import java.util.HashMap;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;

import com.google.code.activetemplates.bind.Bindings;
import com.google.code.activetemplates.script.CompiledScript;
import com.google.code.activetemplates.script.ScriptingAction;
import com.google.code.activetemplates.script.ScriptingContext;
import com.google.code.activetemplates.script.ScriptingProvider;

/**
 * Scripting provider which uses mozilla rhino api directly.
 * 
 * @author sleepless
 *
 */
public class RhinoScriptingProvider implements ScriptingProvider {
    
    static{
        ContextFactory.initGlobal(new ContextFactory (){
            protected boolean hasFeature(Context ctx, int f) {
                
                // we are using shared scope
                if(f == Context.FEATURE_DYNAMIC_SCOPE) return true;
                return super.hasFeature(ctx, f);
            }
        });
    }
    
    private GlobalBindings globalBindings;
    private volatile Map<String, CompiledScript> scriptCache;
    
    public RhinoScriptingProvider(){
        // TODO take base scripts as an argument
        
        scriptCache = new HashMap<String, CompiledScript>();

        ContextFactory.getGlobal().call(new ContextAction(){
            public Object run(Context cx) {
                Scriptable shared = cx.initStandardObjects(new ImporterTopLevel(cx));
                globalBindings = new GlobalBindings(shared);

                // TODO compile and eval base scripts against shared scope
                return null;
            }
        });
    }

    public void call(final ScriptingAction sa) {
        ContextFactory.getGlobal().call(new ContextAction(){
            public Object run(Context cx) {
                RhinoScriptingContext rsc = new RhinoScriptingContext(cx);
                try {
                    sa.call(rsc);
                } finally {
                    rsc.close();
                }
                return null;
            }
        });
    }

    public Bindings createBindings(ScriptingContext sc) {
        Context cx = getContext(sc);
        
        Scriptable scope = cx.newObject(globalBindings.getScope());
        scope.setParentScope(globalBindings.getScope());
        
        return new RhinoBindings((RhinoScriptingContext)sc, scope, globalBindings);
    }

    public Bindings createBindings(Bindings b) {
        Context cx = getContext(b);
        
        Scriptable scope = cx.newObject(globalBindings.getScope());
        scope.setParentScope(getScope(b));
        
        return new RhinoBindings(getScriptingContext(b), scope, (RhinoBindings)b);
    }

    public CompiledScript compile(ScriptingContext sc, String script) {
        String key = script + "\n" + getLocation(sc);
        CompiledScript s = scriptCache.get(key); 
        if(s == null) {
            synchronized(scriptCache) {
                s = scriptCache.get(key);
                if(s == null) {
                    s = doCompile(getContext(sc), script, getLocation(sc));
                    scriptCache.put(key, s);
                }
            }
        }
        
        return s;
    }
    
    public Object eval(String script, Bindings b) {
        Object res = doCompile(getContext(b), script, getScriptingContext(b).getLocation())
            .exec(b);
        return res;

    }

    public boolean evalBoolean(String script, Bindings b) {
        Object o = eval(script, b);
        return o == Boolean.TRUE;
    }

    public String evalString(String script, Bindings b) {
        Object res = eval(script, b);
        return res == null ?  null : res.toString();
    }
    
    private CompiledScript doCompile(Context ctx, String src, String location) {
        Script s = ctx.compileString(src, location, 0, null);
        return new RhinoScript(s);
    }
    
    private Context getContext(ScriptingContext sc) {
        RhinoScriptingContext rsc = (RhinoScriptingContext) sc;
        return rsc.getContext();
    }

    private Context getContext(Bindings b) {
        RhinoBindings rb = (RhinoBindings) b;
        return rb.getScriptingContext().getContext();
    }
    
    private RhinoScriptingContext getScriptingContext(Bindings b) {
        RhinoBindings rb = (RhinoBindings) b;
        return rb.getScriptingContext();
    }
    
    private Scriptable getScope(Bindings b){
        RhinoBindings rb = (RhinoBindings) b;
        return rb.getScope();
    }
    
    private String getLocation(ScriptingContext sc) {
        RhinoScriptingContext rsc = (RhinoScriptingContext) sc;
        return rsc.getLocation();
    }
    
    private class RhinoScript implements CompiledScript {

        private Script script;

        public RhinoScript(Script script) {
            this.script = script;
        }

        public Object exec(Bindings b) {
            Object res = script.exec(getContext(b), getScope(b));
            if(res == null || res == Context.getUndefinedValue()) return null;
            return Context.jsToJava(res, Object.class);
        }

    }

    
}
