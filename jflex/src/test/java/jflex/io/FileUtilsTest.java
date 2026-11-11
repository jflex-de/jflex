/*
 * Copyright (C) 2019, Régis Décamps
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.io;

import static jflex.io.FileUtils.getRelativePath;
import static jflex.io.FileUtils.slashify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import org.junit.jupiter.api.Test;

public class FileUtilsTest {

  @Test
  public void test_getRelativePath_fileInDir() {
    File dir = new File("/a/b/c");
    File f = new File("/a/b/c/d/foo.bar");
    assertEquals("d/foo.bar", getRelativePath(dir, f));
  }

  @Test
  public void test_getRelativePath_fileNotInDir() {
    File dir = new File("/a/b/c");
    File f = new File("/d/e/f/foo.bar");
    assertEquals("/d/e/f/foo.bar", getRelativePath(dir, f));
  }

  @Test
  public void test_getRelativePath_sameStart() {
    File dir = new File("/a/b/c");
    File f = new File("/a/b/c.txt");
    assertEquals("/a/b/c.txt", getRelativePath(dir, f));
  }

  @Test
  public void test_slashify() {
    assertEquals("C:/u0022.txt", slashify("C:\\u0022.txt"));
  }
}
