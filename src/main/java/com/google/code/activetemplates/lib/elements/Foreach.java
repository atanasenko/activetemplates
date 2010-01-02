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

package com.google.code.activetemplates.lib.elements;

import java.util.Queue;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.events.EndElementEvent;
import com.google.code.activetemplates.events.StartElementEvent;
import com.google.code.activetemplates.events.TemplateEvent;
import com.google.code.activetemplates.impl.handlers.DefaultHandlerSPI;
import com.google.code.activetemplates.util.ObjectIterator;
import com.google.code.activetemplates.util.TemplateUtils;

/**
 * Iterates over Map, Iterable, Array or a single Object passed to data
 * attribute
 * 
 * @author sleepless
 * 
 */
public class Foreach implements ElementHandler {

    public static final QName TAG = new QName(
            DefaultHandlerSPI.NAMESPACE_STDLIB, "foreach");

    private static final QName ATTR_DATA = new QName("data");
    private static final QName ATTR_VALUE = new QName("value");
    private static final QName ATTR_INDEX = new QName("index");
    private static final QName ATTR_KEY = new QName("key");

    public Outcome processStart(StartElementEvent e) throws XMLStreamException {

        // attributes
        String dValue = TemplateUtils.getAttribute(e, ATTR_DATA);
        String oName = TemplateUtils.getAttribute(e, ATTR_VALUE);
        String iName = TemplateUtils.getAttribute(e, ATTR_INDEX, null);
        String kName = TemplateUtils.getAttribute(e, ATTR_KEY, null);

        // read content
        Queue<XMLEvent> q = TemplateUtils.readChildren(e.getTemplateContext(),
                false);

        // data
        Object data = e.parseExpression(dValue, Object.class);
        ObjectIterator oit = ObjectIterator.create(data);

        e.getTemplateContext().queueAction(
                new ForeachAction(oit, q, oName, iName, kName));

        return null;
    }

    public Outcome processEnd(EndElementEvent e) {
        return null;
    }

    private static class ForeachAction implements
            com.google.code.activetemplates.events.Action {

        private ObjectIterator oit;
        private Queue<XMLEvent> tree;
        private String[] vars;

        ForeachAction(ObjectIterator oit, Queue<XMLEvent> tree, String oName,
                String iName, String kName) {
            this.oit = oit;
            this.tree = tree;

            vars = new String[] { oName, iName, kName };
        }

        public void execute(TemplateEvent te) {

            if (oit.next()) {

                // setup scope
                te.setExpressionValue(vars[0], oit.getObject());
                if(vars[1] != null) te.setExpressionValue(vars[1], oit.getIndex());
                if(vars[2] != null) te.setExpressionValue(vars[2], oit.getKey());
                
                for(XMLEvent e: tree) {
                    te.queueEvent(e);
                }
                te.getTemplateContext().queueAction(this);
            }

        }

    }

}
