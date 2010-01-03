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

package com.google.code.activetemplates.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import com.google.code.activetemplates.EventStream;
import com.google.code.activetemplates.events.StartElementEvent;

public final class TemplateUtils {

    /**
     * Skip all elements until current elements's end tag is reached.
     * 
     * @param te
     * @param skipEnd
     *            - whether to skip end tag itself, useful from attribute events
     * @throws XMLStreamException
     */
    public static void skipChildren(EventStream tc, boolean skipEnd)
            throws XMLStreamException {
        readElements(tc, 1, skipEnd, null);
    }

    /**
     * Skip all elements until parent tag's end is encountered
     * 
     * @param tc
     * @throws XMLStreamException
     */
    public static void skipSiblings(EventStream tc)
            throws XMLStreamException {
        readElements(tc, 2, true, null);
    }

    /**
     * Read all elements into a queue until current elements's end tag is
     * reached.
     * 
     * @param tc
     * @param readEnd
     *            - whether to read end tag itself, useful from attribute events
     * @return queue
     * @throws XMLStreamException
     */
    public static Queue<XMLEvent> readChildren(EventStream tc,
            boolean readEnd) throws XMLStreamException {
        Queue<XMLEvent> q = new ArrayDeque<XMLEvent>();
        readElements(tc, 1, readEnd, q);
        return q;
    }

    /**
     * Read all elements into a queue until parent tag's end is encountered
     * 
     * @param te
     * @return queue
     * @throws XMLStreamException
     */
    public static Queue<XMLEvent> readSiblings(EventStream tc)
            throws XMLStreamException {
        Queue<XMLEvent> q = new ArrayDeque<XMLEvent>();
        readElements(tc, 2, true, q);
        return q;
    }

    // skip elements until level reaches 0
    private static void readElements(EventStream tc, int initialLevel,
            boolean readEnd, Queue<XMLEvent> q) throws XMLStreamException {

        while (tc.hasNextEvent()) {
            XMLEvent e = tc.peekEvent();

            if (e.isStartElement()) {
                initialLevel++;
            } else if (e.isEndElement()) {
                initialLevel--;
            }

            if (initialLevel == 0) {
                // do not remove the event if we need to process it later
                if (readEnd) {
                    e = tc.nextEvent();
                    if (q != null)
                        q.offer(e);
                }
                break;
            }
            e = tc.nextEvent();
            if (q != null)
                q.offer(e);

        }

    }

    /**
     * Returns value of the specified attribute or defValue, if no such
     * attribute is specified
     * 
     * @param se
     * @param attribute
     * @param defValue
     * @return
     */
    public static String getAttribute(StartElementEvent se, QName attribute,
            String defValue) {
        Attribute a = se.getEvent().getAttributeByName(attribute);
        if (a == null)
            return defValue;
        return a.getValue();
    }

    /**
     * Returns value of the specified attribute, or throws an
     * IllegalArgumentException if no such attribute is specified
     * 
     * @param se
     * @param attribute
     * @return
     */
    public static String getAttribute(StartElementEvent se, QName attribute) {
        Attribute a = se.getEvent().getAttributeByName(attribute);
        if (a == null)
            throw new IllegalArgumentException("Attribute "
                    + attribute.getLocalPart() + " not specified");
        return a.getValue();
    }
    
    /**
     * Gets informal attributes from the specified element which will not
     * contain formal attributes with specified names
     * 
     * @param se
     * @param formalAttributes
     * @return
     */
    public static List<Attribute> getInformalAttributes(StartElementEvent se, QName ... formalAttributes) {
        Set<QName> formalNames = new HashSet<QName>(formalAttributes.length);
        Collections.addAll(formalNames, formalAttributes);
        
        List<Attribute> informalAttributes = new ArrayList<Attribute>();
        
        @SuppressWarnings("unchecked")
        Iterator<Attribute> ait = se.getEvent().getAttributes();
        while(ait.hasNext()) {
            Attribute at = ait.next();
            if(!formalNames.contains(at.getName()))
                informalAttributes.add(at);
        }

        return informalAttributes;
    }
}
