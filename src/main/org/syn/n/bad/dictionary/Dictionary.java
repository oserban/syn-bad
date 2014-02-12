/*******************************************************************************
 * Copyright (c) Ovidiu Serban, ovidiu@roboslang.org
 *               web:http://ovidiu.roboslang.org/
 * All Rights Reserved. Use is subject to license terms.
 *
 * This file was part of AgentSlang Project (http://agent.roboslang.org/),
 * now is available as an independent library on GitHub:
 * 		https://github.com/ovidiusx/syn-bad
 *
 * Syn!bad is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 *
 * The usage of this project makes mandatory the authors citation in any scientific publication or technical reports.
 * Please use the following articles:
 *
 * Syn!bad citation:
 *
 * O. Serban. Syn!bad: A Synonym-Based Regular Expression Extension For Knowledge Extraction Tasks,
 * Studia Universitatis "Babes-Bolyai", Series Informatica, Volume LIX, Number 1 (June 2014),
 * pages 5 - 15, 2014.
 *
 * AgentSlang platform citation:
 *
 * O. Serban and A. Pauchet. AgentSlang: A Fast and Reliable Platform for Distributed Interactive
 * Systems, International Conference on Intelligent Computer Communication and Processing (ICCP),
 * pages 35 - 42, IEEE CPS, 2013.
 *
 * For websites or research projects the AgentSlang website and logo needs to be linked in a visible area.
 ******************************************************************************/

package org.syn.n.bad.dictionary;

import org.syn.n.bad.dictionary.extensions.AbstractDictionaryExtension;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Ovidiu Serban, ovidiu@roboslang.org
 * @version 1, 2/24/13
 */
public class Dictionary {
    private static final String TAG_EXTENSIONS = "extensions";
    private static final String TAG_EXTENSION = "extension";
    private static final String ATT_NAME = "name";
    private static final String ATT_PREFIX = "prefix";
    private static final String TAG_CONFIG = "config";
    private final List<AbstractDictionaryExtension> dictionaryExtensions = new LinkedList<AbstractDictionaryExtension>();
    private AbstractDictionaryExtension defaultDictionary = null;

    private Dictionary(InputStream configInput) throws DictionaryException {
        setupDictionaryExtensions(configInput);
    }

    public static Dictionary getInstance(InputStream configInput) throws DictionaryException {
        return new Dictionary(configInput);
    }

    public static Dictionary getInstance() throws DictionaryException {
        return getInstance(Dictionary.class.getResourceAsStream("dictionaryExtensions.xml"));
    }

    public static void main(String[] args) {
        try {
            Dictionary instance = getInstance();

            printSynset(instance, "synonym");
            printSynset(instance, "synonyms");
            printSynset(instance, "is");
            printSynset(instance, "are");
            printSynset(instance, "synonimic");
        } catch (DictionaryException e) {
            e.printStackTrace();
        }
    }

    private static void printSynset(Dictionary dictionary, String term) {
        try {
            Word word = dictionary.getWord(Locale.US, term, POS.values());

            System.out.println("Synonyms for " + word.getWord());
            for (String item : word.getSynsetIds()) {
                System.out.println("\t" + item);
            }
            System.out.println("End listing ...");
        } catch (DictionaryException e) {
            e.printStackTrace();
        }
    }

    private void setupDictionaryExtensions(InputStream configInput) throws DictionaryException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(configInput);
            doc.getDocumentElement().normalize();

            NodeList extensionRootList = doc.getElementsByTagName(TAG_EXTENSIONS);
            for (int i = 0; i < extensionRootList.getLength(); i++) {
                if (extensionRootList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    NodeList extensionList = ((Element) extensionRootList.item(i)).getElementsByTagName(TAG_EXTENSION);

                    for (int j = 0; j < extensionList.getLength(); j++) {
                        Element extension = (Element) extensionList.item(j);
                        String name = extension.getAttribute(ATT_NAME).trim();
                        String prefix = extension.getAttribute(ATT_PREFIX).trim();
                        String config = null;

                        NodeList configList = extension.getElementsByTagName(TAG_CONFIG);
                        for (int k = 0; k < configList.getLength(); k++) {
                            if (configList.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                config = configList.item(k).getTextContent().trim();
                                break;
                            }
                        }
                        setupExtension(name, config, prefix);
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            throw new IllegalArgumentException(e);
        } catch (SAXException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void setupExtension(String name, String config, String prefix) throws DictionaryException {
        if (name != null) {
            try {
                Constructor constructor = Class.forName(name).getDeclaredConstructor(String.class, String.class);
                constructor.setAccessible(true);
                AbstractDictionaryExtension extension;
                if (config != null) {
                    extension = (AbstractDictionaryExtension) constructor.newInstance(prefix, config);
                } else {
                    extension = (AbstractDictionaryExtension) constructor.newInstance(prefix, null);
                }

                if ("default".equals(prefix)) {
                    defaultDictionary = extension;
                } else {
                    dictionaryExtensions.add(extension);
                }
            } catch (ClassNotFoundException e) {
                throw new DictionaryException(e);
            } catch (NoSuchMethodException e) {
                throw new DictionaryException(e);
            } catch (InvocationTargetException e) {
                throw new DictionaryException(e);
            } catch (InstantiationException e) {
                throw new DictionaryException(e);
            } catch (IllegalAccessException e) {
                throw new DictionaryException(e);
            }
        }
    }

    public Word getWord(Locale language, String word, POS... pos) throws DictionaryException {
        Set<String> result = new HashSet<String>();

        List<POS> restrictions;
        if (pos == null || pos.length == 0) {
            restrictions = POS.getAllPOS();
        } else {
            restrictions = Arrays.asList(pos);
        }

        for (AbstractDictionaryExtension extension : dictionaryExtensions) {
            result.addAll(extension.getSynsetIDs(language, word, restrictions));
        }

        if (result.isEmpty() && defaultDictionary != null) {
            result.addAll(defaultDictionary.getSynsetIDs(language, word, restrictions));
        }

        return new Word(word, result);
    }
}
