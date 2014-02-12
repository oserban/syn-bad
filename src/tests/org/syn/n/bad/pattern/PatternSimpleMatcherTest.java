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

package org.syn.n.bad.pattern;

import org.junit.Before;
import org.junit.Test;
import org.syn.n.bad.annotation.TextAnnotation;
import org.syn.n.bad.annotation.TextToken;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * @author Ovidiu Serban, ovidiu@roboslang.org
 * @version 1/6/13
 */
public class PatternSimpleMatcherTest {
    private Matcher matcher;

    @Before()
    public void setup() {
        matcher = new Matcher();
        matcher.addMatcher(new PatternMatcher("1", "xxx yyy zzz"));
        matcher.addMatcher(new PatternMatcher("2", "xxx yyy"));
        matcher.addMatcher(new PatternMatcher("3", "xxx aaa zzz"));
    }

    @Test()
    public void testSimpleMatcher1() {
        TextAnnotation tokens = new TextAnnotation(4);
        tokens.addTextToken(new TextToken("aaa"));
        tokens.addTextToken(new TextToken("xxx"));
        tokens.addTextToken(new TextToken("yyy"));
        tokens.addTextToken(new TextToken("zzz"));

        MatchResult result = matcher.match(tokens, 0, tokens.size());
        assertNull(result.getTemplateID());

        result = matcher.match(tokens, 1, tokens.size());
        assertSame("1", result.getTemplateID());
        assertSame(3, result.getMatchedTokens());
    }

    @Test()
    public void testSimpleMatcher2() {
        TextAnnotation tokens = new TextAnnotation(4);
        tokens.addTextToken(new TextToken("xxx"));
        tokens.addTextToken(new TextToken("aaa"));
        tokens.addTextToken(new TextToken("yyy"));
        tokens.addTextToken(new TextToken("zzz"));

        MatchResult result = matcher.match(tokens, 0, tokens.size());

        assertSame("1", result.getTemplateID());
        assertSame(4, result.getMatchedTokens());
    }

    @Test()
    public void testSimpleMatcher3() {
        TextAnnotation tokens = new TextAnnotation(5);
        tokens.addTextToken(new TextToken("xxx"));
        tokens.addTextToken(new TextToken("aaa"));
        tokens.addTextToken(new TextToken("aaa"));
        tokens.addTextToken(new TextToken("yyy"));
        tokens.addTextToken(new TextToken("zzz"));

        MatchResult result = matcher.match(tokens, 0, tokens.size());

        assertSame("1", result.getTemplateID());
        assertSame(5, result.getMatchedTokens());
    }

    @Test()
    public void testSimpleMatcher4() {
        TextAnnotation tokens = new TextAnnotation(6);
        tokens.addTextToken(new TextToken("xxx"));
        tokens.addTextToken(new TextToken("aaa"));
        tokens.addTextToken(new TextToken("aaa"));
        tokens.addTextToken(new TextToken("aaa"));
        tokens.addTextToken(new TextToken("yyy"));
        tokens.addTextToken(new TextToken("zzz"));

        MatchResult result = matcher.match(tokens, 0, tokens.size());

        assertNull(result.getTemplateID());
        assertSame(-1, result.getMatchedTokens());
    }

    @Test()
    public void testSimpleMatcher5() {
        TextAnnotation tokens = new TextAnnotation(5);
        tokens.addTextToken(new TextToken("xxx"));
        tokens.addTextToken(new TextToken("aaa"));
        tokens.addTextToken(new TextToken("zzz"));
        tokens.addTextToken(new TextToken("xxx"));
        tokens.addTextToken(new TextToken("yyy"));

        MatchResult result = matcher.match(tokens, 0, tokens.size());

        assertSame("3", result.getTemplateID());
        assertSame(3, result.getMatchedTokens());

        result = matcher.match(tokens, 3, tokens.size());

        assertSame("2", result.getTemplateID());
        assertSame(2, result.getMatchedTokens());
    }
}
