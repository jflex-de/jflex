/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.core;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.io.FileNotFoundException;
import jflex.testing.TestFileUtils;
import org.junit.Ignore;
import org.junit.Test;

/**
 * SkeletonTest
 *
 * @author Gerwin Klein
 * @author Régis Décamps
 * @version JFlex 1.8.0-SNAPSHOT
 */
public class SkeletonTest {

  @Test
  public void testReplace() {
    assertEquals(Skeleton.replace("bla ", "blub", "bla blub bla "), "blubblub blub");
  }

  @Test
  public void testMakePrivate() {
    Skeleton.makePrivate();
    for (int i = 0; i < Skeleton.line.length; i++) {
      assertEquals(Skeleton.line[i].indexOf("public"), -1);
    }
  }

  @Test
  public void readSkelFile_maven() {
    assumeTrue(!TestFileUtils.BAZEL_RUNFILES);
    File skeletonFile = new File("src/main/jflex/skeleton.nested");
    Skeleton.readSkelFile(skeletonFile);
    checkDefaultSkeleton();
  }

  @Test
  @Ignore // fix loading resources
  public void readSkelFile_bazel() throws FileNotFoundException {
    assumeTrue(TestFileUtils.BAZEL_RUNFILES);
    File skeletonFile = TestFileUtils.open("//jflex", "jflex/skeleton.nested");
    Skeleton.readSkelFile(skeletonFile);
    checkDefaultSkeleton();
  }

  private void checkDefaultSkeleton() {
    assertTrue(Skeleton.line[3].indexOf("java.util.Stack") > 0);
    Skeleton.readDefault();
    assertEquals(Skeleton.line[3].indexOf("java.util.Stack"), -1);
  }
}
