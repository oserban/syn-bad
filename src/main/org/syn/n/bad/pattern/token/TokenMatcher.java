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

package org.syn.n.bad.pattern.token;

import org.syn.n.bad.annotation.TextAnnotation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ovidiu Serban, ovidiu@roboslang.org
 * @version 1, 1/5/13
 */
public abstract class TokenMatcher implements Comparable<TokenMatcher> {
    public static final int PRIORITY_SKIP = 0;
    public static final int PRIORITY_CONSUME = 1;
    public static final int PRIORITY_MANDATORY = 2;
    public static final int PRIORITY_OPTIONAL = 3;

    protected String pattern;
    protected int priority;
    private boolean isFinal = false;

    private String variableLabel = null;

    private List<TokenMatcher> matcherList = new LinkedList<TokenMatcher>();

    protected TokenMatcher(String pattern, boolean isMandatory) {
        this.pattern = pattern;
        setupPriority(isMandatory);
    }

    protected void setupPriority(boolean isMandatory) {
        if (isMandatory) {
            priority = PRIORITY_MANDATORY;
        } else {
            priority = PRIORITY_OPTIONAL;
        }
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public int getPriority() {
        return priority;
    }

    public String toString() {
        return pattern;
    }

    protected void setVariableLabel(String label) {
        this.variableLabel = label;
    }

    public boolean hasVariableLabel() {
        return this.variableLabel != null;
    }

    public String getVariableLabel() {
        return this.variableLabel;
    }

    public void addNext(TokenMatcher matcher) {
        matcherList.add(matcher);
    }

    public void sortMatchers() {
        Collections.sort(matcherList);
    }

    public List<TokenMatcher> getMatcherList() {
        return matcherList;
    }

    public int compareTo(TokenMatcher o) {
        int cmp = o.priority - priority;
        return cmp != 0 ? cmp : o.pattern.compareTo(pattern);
    }

    public abstract boolean match(TextAnnotation tokens, int index);
}