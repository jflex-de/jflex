/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.generator;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

/**
 * Some unit tests for the jflex Emitter class
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.0-SNAPSHOT
 */
public class EmitterTest {

  @Test
  public void testJavadoc() {
    StringBuilder usercode = new StringBuilder("/* some *** comment */");
    assertTrue(!jflex.generator.Emitter.endsWithJavadoc(usercode));
    usercode.append("import bla;  /** javadoc /* */  ");
    assertTrue(jflex.generator.Emitter.endsWithJavadoc(usercode));
    usercode.append("bla");
    assertTrue(!jflex.generator.Emitter.endsWithJavadoc(usercode));
    usercode.setLength(usercode.length() - "bla".length());
    String nonJavadocComment = "\n/* blah */\n";
    usercode.append(nonJavadocComment);
    assertTrue(!jflex.generator.Emitter.endsWithJavadoc(usercode));
    usercode.setLength(usercode.length() - nonJavadocComment.length());
    List<String> annotations =
        Arrays.asList(
            "@Deprecated",
            "@SuppressWarnings(\"deprecation\")",
            "@SomeOtherSingleArrayParamAnnotation( {\"one\", \"two and \\\"three\\\"\" })",
            "@java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE,\n"
                + "                              java.lang.annotation.ElementType.FIELD,\n"
                + "                              java.lang.annotation.ElementType.METHOD,\n"
                + "                              java.lang.annotation.ElementType.PARAMETER,\n"
                + "                              java.lang.annotation.ElementType.CONSTRUCTOR,\n"
                + "                              java.lang.annotation.ElementType.LOCAL_VARIABLE})\n",
            " @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)",
            "@ClassPreamble (\n"
                + "    author = \"John Doe\",\n"
                + "    date = \"3/17/2002\",\n"
                + "    currentRevision = 6,\n"
                + "    lastModified = \"4/12/2004\",\n"
                + "    lastModifiedBy = \"Jane Doe\",\n"
                + "    reviewers = { \"Alice\", \"Bob\",\n"
                + "                  \"Cindy\"}\n"
                + ")",
            "@org.example.Custom_76\n"
                + "   ( a_333= 5L , b___ = \n"
                + "     189,f=SOMETHING_ELSE, c=')', d=\"(parenthesized)\"\n"
                + ", e=.378e-8f, f=0x88_87,g=0b1111_0011, h=\"/** javadoc comment */\" )",
            "@A(\"/* non-javadoc comment */\")");
    Collections.shuffle(annotations);
    for (String annotation : annotations) {
      usercode.append("\n  ").append(annotation);
      assertTrue(
          "Problematic annotation: '" + annotation + "' in '" + usercode.toString() + "'",
          jflex.generator.Emitter.endsWithJavadoc(usercode));
    }
    usercode.append("\n").append(nonJavadocComment);
    assertTrue(!Emitter.endsWithJavadoc(usercode));
  }
}
