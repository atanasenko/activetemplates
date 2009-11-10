package com.google.code.activetemplates.lib.elements;

import javax.xml.namespace.QName;

import com.google.code.activetemplates.TemplateConstants;
import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.events.EndElementEvent;
import com.google.code.activetemplates.events.StartElementEvent;

/**
 * Element which start and end tags will be omitted from output.
 * 
 * Useful when an included template in a separate xml file doesn't have a 
 * single root element.
 * 
 * @author sleepless
 *
 */
public class Container implements ElementHandler {

    public static final QName TAG = new QName(TemplateConstants.NAMESPACE_STDLIB, "container");

    public Outcome processStart(StartElementEvent e) {
        return Outcome.PROCESS_CHILDREN;
    }

    public Outcome processEnd(EndElementEvent e) {
        return Outcome.PROCESS_SIBLINGS;
    }

}
