package com.google.code.activetemplates.lib.elements.conditional;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;

import com.google.code.activetemplates.events.EndElementEvent;
import com.google.code.activetemplates.events.StartElementEvent;
import com.google.code.activetemplates.impl.handlers.BuiltinHandlerSPI;

public class ElseEl extends ConditionalEl {

    public static final QName ELEMENT = new QName(
            BuiltinHandlerSPI.NAMESPACE_STDLIB, "else");

    private static final QName ATTR_CONDITION = new QName("condition");

    public QName[] getElements() {
        return new QName[]{ ELEMENT };
    }

    public Outcome processStart(StartElementEvent e) {

        ConditionScope cScope = getConditionScope(e);
        if(cScope == null || cScope.isInner()) {
            throw new IllegalStateException("Else without a conditional scope");
        } else if(cScope.getConditionValue() == null) {
            throw new IllegalStateException("Value for conditional scope is not initialized for else/else if");
        }
        
        Attribute a = e.getEvent().getAttributeByName(ATTR_CONDITION);

        boolean cond = !cScope.getConditionValue().booleanValue();
        if(cond && a != null) {
            cond = e.parseExpression(a.getValue(), Boolean.class).booleanValue();
        }
        
        // do processing
        return processIf(e, cond);
    }

    public void processEnd(EndElementEvent e) {
        processEndIf(e);
    }

}