/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.version;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

/** Test for {@link Version}. */
public class VersionTest {

  @Test
  public void version_major() {
    Version v = new Version("42");
    assertEquals(42, v.major);
    assertEquals(-1, v.minor);
    assertEquals(-1, v.patch);
  }

  @Test
  public void version_major_minor() {
    Version v = new Version("1.2");
    assertEquals(1, v.major);
    assertEquals(2, v.minor);
    assertEquals(-1, v.patch);
  }

  @Test
  public void version_major_patch() {
    Version v = new Version("1.2.3");
    assertEquals(1, v.major);
    assertEquals(2, v.minor);
    assertEquals(3, v.patch);
  }

  @Test
  public void compare_eaxact() {
    SortedSet<Version> versions = new TreeSet<>(Version.EXACT_VERSION_COMPARATOR);
    versions.add(new Version("1.2.1"));
    versions.add(new Version("1.2.0"));
    versions.add(new Version("1.0"));
    versions.add(new Version("10.0"));
    assertEquals(
        Arrays.asList(
            new Version(1, 0), new Version(1, 2, 0), new Version(1, 2, 1), new Version(10, 0)),
        new ArrayList<>(versions));
  }

  @Test
  public void compare_majorMinor() {
    SortedSet<Version> versions = new TreeSet<>(Version.MAJOR_MINOR_COMPARATOR);
    versions.add(new Version("1.2.1"));
    versions.add(new Version("1.2.0"));
    versions.add(new Version("1.0"));
    versions.add(new Version("10.0"));
    assertEquals(
        Arrays.asList(new Version(1, 0), new Version(1, 2), new Version(10, 0)),
        new ArrayList<>(versions));
  }
}
