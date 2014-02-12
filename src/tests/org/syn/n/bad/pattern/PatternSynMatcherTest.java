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
import org.syn.n.bad.annotation.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Serban, ovidiu@roboslang.org
 * @version 1/6/13
 */
public class PatternSynMatcherTest {
    private Matcher matcher;

    @Before()
    public void setup() {
        matcher = new Matcher();
        matcher.addMatcher(new PatternMatcher("1", "[animal] goes wild"));
        matcher.addMatcher(new PatternMatcher("2", "brute goes wild"));
        matcher.addMatcher(new PatternMatcher("3", "feeling [good|RB*#attribute]"));
    }

    @Test()
    public void testSynMatcherNoRestriction() {
        TextAnnotation tokens = new TextAnnotation(4);
        tokens.addTextToken(new TextToken("brute"));
        tokens.addTextToken(new TextToken("goes"));
        tokens.addTextToken(new TextToken("wild"));
        tokens.addTextToken(new TextToken("now"));

        MatchResult result = matcher.match(tokens, 0, tokens.size());
        assertEquals("1", result.getTemplateID());
        assertEquals(3, result.getMatchedTokens());
    }

    @Test()
    public void testSynMatcherRestriction1() {
        TextAnnotation tokens = new TextAnnotation(4);
        tokens.addTextToken(new TextToken("brute"));
        tokens.addTextToken(new TextToken("goes"));
        tokens.addTextToken(new TextToken("wild"));
        tokens.addTextToken(new TextToken("now"));

        Annotation annotation = new Annotation();
        annotation.addToken(new AnnotationToken(0, TextAnnotationConstants
                                                           .transformAnnotationLabel(TextAnnotationConstants
                                                                                             .getLevel(TextAnnotationConstants.POS),
                                                                                     "NN")));
        tokens.addAnnotation(TextAnnotationConstants.getLevel(TextAnnotationConstants.POS), annotation);

        MatchResult result = matcher.match(tokens, 0, tokens.size());
        assertEquals("1", result.getTemplateID());
        assertEquals(3, result.getMatchedTokens());
    }

    @Test()
    public void testSynMatcherRestriction2() {
        TextAnnotation tokens = new TextAnnotation(4);
        tokens.addTextToken(new TextToken("brute"));
        tokens.addTextToken(new TextToken("goes"));
        tokens.addTextToken(new TextToken("wild"));
        tokens.addTextToken(new TextToken("now"));

        Annotation annotation = new Annotation();
        annotation.addToken(new AnnotationToken(0, TextAnnotationConstants
                                                           .transformAnnotationLabel(TextAnnotationConstants
                                                                                             .getLevel(TextAnnotationConstants.POS),
                                                                                     "JJ")));
        tokens.addAnnotation(TextAnnotationConstants.getLevel(TextAnnotationConstants.POS), annotation);

        MatchResult result = matcher.match(tokens, 0, tokens.size());
        assertEquals("2", result.getTemplateID());
        assertEquals(3, result.getMatchedTokens());
    }

    @Test()
    public void testSynMatcherRestriction3() {
        TextAnnotation tokens = new TextAnnotation(3);
        tokens.addTextToken(new TextToken("feeling"));
        tokens.addTextToken(new TextToken("well"));
        tokens.addTextToken(new TextToken("now"));

        Annotation annotation = new Annotation();
        annotation.addToken(new AnnotationToken(1, TextAnnotationConstants
                                                           .transformAnnotationLabel(TextAnnotationConstants
                                                                                             .getLevel(TextAnnotationConstants.POS),
                                                                                     "RB")));
        tokens.addAnnotation(TextAnnotationConstants.getLevel(TextAnnotationConstants.POS), annotation);

        MatchResult result = matcher.match(tokens, 0, tokens.size());
        assertEquals("3", result.getTemplateID());
        assertEquals(2, result.getMatchedTokens());

        assertEquals("well", result.getMatchedVars().get("#attribute"));
    }

    @Test()
    public void testYeah() {
        Matcher matcher1 = new Matcher();
        matcher1.addMatcher(new PatternMatcher("1", "[car]"));

        TextAnnotation tokens = new TextAnnotation(1);
        tokens.addTextToken(new TextToken("yeah"));

        MatchResult result = matcher1.match(tokens, 0, tokens.size());
        assertNull(result.getTemplateID());
        assertEquals(-1, result.getMatchedTokens());
    }
}
