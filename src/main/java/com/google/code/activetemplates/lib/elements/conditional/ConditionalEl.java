package com.google.code.activetemplates.lib.elements.conditional;

import javax.xml.namespace.QName;

import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.events.EndElementEvent;
import com.google.code.activetemplates.events.StartElementEvent;
import com.google.code.activetemplates.events.TemplateEvent;
import com.google.code.activetemplates.impl.handlers.BuiltinHandlerSPI;

public class ConditionalEl implements ElementHandler {
    
    public static final QName ELEMENT = new QName(
            BuiltinHandlerSPI.NAMESPACE_STDLIB, "conditional");

    private static final String CONDITION_SCOPE = ConditionalEl.class.getName() + ".scope";

    public QName[] getElements() {
        return new QName[]{ ELEMENT };
    }

    public Outcome processStart(StartElementEvent e) {
        newConditionScope(e);
        return null;
    }

    public Outcome processEnd(EndElementEvent e) {
        return null;
    }
    
    protected Outcome processIf(StartElementEvent e, boolean cond) {
        ConditionScope cScope = getConditionScope(e);
        
        cScope.setConditionValue(Boolean.valueOf(cond));
        cScope.setInner(true);
        
        if(!cond) {
            return Outcome.PROCESS_SIBLINGS;
        }

        return null;
    }

    protected Outcome processEndIf(EndElementEvent e) {
        getConditionScope(e).setInner(false);
        return null;
    }
    
    protected ConditionScope getConditionScope(TemplateEvent e) {
        return e.getEnvironment().get(CONDITION_SCOPE, ConditionScope.class);
    }
    
    protected ConditionScope newConditionScope(TemplateEvent e) {
        ConditionScope cScope = new ConditionScope();
        e.getEnvironment().put(CONDITION_SCOPE, cScope);
        return cScope;
    }
    
    public static class ConditionScope {
        
        private Boolean conditionValue;
        private boolean inner;
        
        public Boolean getConditionValue() {
            return conditionValue;
        }
        public void setConditionValue(Boolean conditionValue) {
            this.conditionValue = conditionValue;
        }
        
        public boolean isInner() {
            return inner;
        }
        public void setInner(boolean inner) {
            this.inner = inner;
        }
        
    }
}
