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

package org.syn.n.bad.annotation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ovidiu Serban, ovidiu@roboslang.org
 * @version 1, 12/9/12
 */

public class Annotation {
    private List<AnnotationToken> tokens = new LinkedList<AnnotationToken>();

    public Annotation() {
    }

    private AnnotationToken lastToken() {
        if (tokens.isEmpty()) {
            return null;
        } else {
            return tokens.get(tokens.size() - 1);
        }
    }

    public void addToken(AnnotationToken token) {
        tokens.add(token);
    }

    public AnnotationToken getFirstToken() {
        if (tokens.isEmpty()) {
            return null;
        } else {
            return tokens.get(0);
        }
    }

    public Collection<AnnotationToken> getTokens(byte label) {
        Collection<AnnotationToken> result = new LinkedList<AnnotationToken>();

        for (AnnotationToken token : tokens) {
            if (token.getAnnotationLabel() == label) {
                result.add(token);
            }
        }

        return result;
    }

    public Collection<AnnotationToken> getTokens(int textTokenIndex) {
        Collection<AnnotationToken> result = new LinkedList<AnnotationToken>();

        for (AnnotationToken token : tokens) {
            if (token.getTextTokenIndex() == textTokenIndex) {
                result.add(token);
            }
        }

        return result;
    }

    public List<AnnotationToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<AnnotationToken> tokens) {
        this.tokens = tokens;
    }
}
