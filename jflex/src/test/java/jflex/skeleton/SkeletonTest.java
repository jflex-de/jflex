/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.skeleton;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.Test;

/**
 * Test {@link Skeleton}
 *
 * @author Gerwin Klein
 * @author Régis Décamps
 * @version JFlex 1.10.0-SNAPSHOT
 */
public class SkeletonTest {

  @Test
  public void testReplace() {
    assertEquals("blubblub blub", Skeleton.replace("bla ", "blub", "bla blub bla "));
  }

  private static boolean containsPublic(Skeleton skeleton) {
    for (int i = 0; i < skeleton.size(); i++) {
      if (skeleton.section(i).contains(" public ")) {
        return true;
      }
    }
    return false;
  }

  @Test
  public void testMakePrivate() {
    Skeleton privateSkeleton = Skeleton.DEFAULT.makePrivate();
    assertFalse(containsPublic(privateSkeleton));
    // makePrivate() does not change the original
    assertTrue(containsPublic(Skeleton.DEFAULT));
  }

  @Test
  public void readSkelFile() {
    Skeleton nested = Skeleton.readSkelFile(new File("src/main/jflex/skeleton.nested"));
    assertTrue(nested.section(3).contains("java.util.Deque"));
    assertFalse(Skeleton.DEFAULT.section(3).contains("java.util.Deque"));
  }
}
