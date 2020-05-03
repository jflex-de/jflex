/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.skeleton;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.io.FileNotFoundException;
import jflex.testing.TestFileUtils;
import org.junit.Test;

/**
 * Test {@link Skeleton}
 *
 * @author Gerwin Klein
 * @author Régis Décamps
 * @version JFlex 1.8.2
 */
public class SkeletonTest {

  @Test
  public void testReplace() {
    assertThat(Skeleton.replace("bla ", "blub", "bla blub bla ")).isEqualTo("blubblub blub");
  }

  @Test
  public void testMakePrivate() {
    Skeleton.makePrivate();
    for (int i = 0; i < Skeleton.line.length; i++) {
      assertThat(Skeleton.line[i]).doesNotContain("public");
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
  public void readSkelFile_bazel() throws FileNotFoundException {
    assumeTrue(TestFileUtils.BAZEL_RUNFILES);
    File skeletonFile = TestFileUtils.open("//jflex/src/main/jflex", "skeleton.nested");
    Skeleton.readSkelFile(skeletonFile);
    checkDefaultSkeleton();
  }

  private static void checkDefaultSkeleton() {
    assertThat(Skeleton.line[3]).contains("java.util.Deque");
    Skeleton.readDefault();
    assertThat(Skeleton.line[3]).doesNotContain("java.util.Deque");
  }
}
