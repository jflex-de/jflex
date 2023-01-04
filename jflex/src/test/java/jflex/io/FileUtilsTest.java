/*
 * Copyright (C) 2019, Régis Décamps
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.io;

import static com.google.common.truth.Truth.assertThat;
import static jflex.io.FileUtils.getRelativePath;
import static jflex.io.FileUtils.slashify;

import java.io.File;
import org.junit.Test;

public class FileUtilsTest {

  @Test
  public void test_getRelativePath_fileInDir() {
    File dir = new File("/a/b/c");
    File f = new File("/a/b/c/d/foo.bar");
    assertThat(getRelativePath(dir, f)).isEqualTo("d/foo.bar");
  }

  @Test
  public void test_getRelativePath_fileNotInDir() {
    File dir = new File("/a/b/c");
    File f = new File("/d/e/f/foo.bar");
    assertThat(getRelativePath(dir, f)).isEqualTo("/d/e/f/foo.bar");
  }

  @Test
  public void test_getRelativePath_sameStart() {
    File dir = new File("/a/b/c");
    File f = new File("/a/b/c.txt");
    assertThat(getRelativePath(dir, f)).isEqualTo("/a/b/c.txt");
  }

  @Test
  public void test_slashify() {
    assertThat(slashify("C:\\u0022.txt")).isEqualTo("C:/u0022.txt");
  }
}
