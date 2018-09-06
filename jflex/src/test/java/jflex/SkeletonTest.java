/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2015  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import junit.framework.TestCase;

/**
 * Tests for {@link Skeleton}.
 *
 * @author Gerwin Klein, Régis Décamps
 * @version JFlex 1.7.0-SNAPSHOT
 */
public class SkeletonTest extends TestCase {

  private Skeleton skeleton;

  public void setUp() throws IOException {
    String charset = "UTF-8";
    File tempFile = File.createTempFile("out", ".tmp");
    PrintWriter out =
        new PrintWriter(
            new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile), charset)));
    skeleton = new Skeleton(out);
  }

  public void testReplace() {
    assertEquals(Skeleton.replace("bla ", "blub", "bla blub bla "), "blubblub blub");
  }

  public void testMakePrivate() {
    skeleton.makePrivate();
    for (int i = 0; i < skeleton.line.length; i++) {
      assertEquals(skeleton.line[i].indexOf("public"), -1);
    }
  }

  public void testReadDefault() {
    skeleton.readSkelFile(new File("src/main/jflex/skeleton.nested"));
    assertTrue(skeleton.line[3].indexOf("java.util.Stack") > 0);
    skeleton.readDefault();
    assertEquals(skeleton.line[3].indexOf("java.util.Stack"), -1);
  }
}
