/*
 * Copyright (C) 2014-2018 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2019 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.version;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import java.util.Comparator;
import java.util.List;

public class Version {

  public static final Comparator<Version> EXACT_VERSION_COMPARATOR = new ExactVersionComparator();
  public static final Comparator<Version> MAJOR_MINOR_COMPARATOR =
      new MajorMinorVersionComparator();

  public final int major;
  public final int minor;
  public final int patch;

  public Version(String version) {
    List<String> parts = Splitter.on(".").splitToList(version);
    this.major = Integer.parseInt(parts.get(0));
    this.minor = parts.size() > 1 ? Integer.parseInt(parts.get(1)) : -1;
    this.patch = parts.size() > 2 ? Integer.parseInt(parts.get(2)) : -1;
  }

  public Version(int major, int minor) {
    this(major, minor, -1);
  }

  public Version(int major, int minor, int patch) {
    this.major = major;
    this.minor = minor;
    this.patch = patch;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Version)) {
      return false;
    }
    Version other = (Version) o;
    return MAJOR_MINOR_COMPARATOR.compare(this, other) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.major, this.minor, this.patch);
  }

  @Override
  public String toString() {
    return makeString('.', true);
  }

  public int getMajor() {
    return major;
  }

  public int getMinor() {
    return minor;
  }

  public String toMajorMinorString() {
    return makeString('.', false);
  }

  public String unicodeClassName() {
    return "Unicode_" + makeString('_', false);
  }

  public String underscoreVersion() {
    return makeString('_', true);
  }

  public boolean isAtLeast(String thresholdVersion) {
    return Version.EXACT_VERSION_COMPARATOR.compare(this, new Version(thresholdVersion)) >= 0;
  }

  private String makeString(char sep, boolean includePatch) {
    StringBuilder v = new StringBuilder();
    v.append(major);
    if (minor != -1) {
      v.append(sep);
      v.append(minor);
    }
    if (includePatch && minor != -1 && patch != -1) {
      v.append(sep);
      v.append(patch);
    }
    return v.toString();
  }

  static class MajorMinorVersionComparator implements Comparator<Version> {
    @Override
    public int compare(Version left, Version right) {
      if (left.major != right.major) {
        return Integer.compare(left.major, right.major);
      }
      if (left.minor != right.minor) {
        return Integer.compare(left.minor, right.minor);
      }
      return 0;
    }
  }

  private static class ExactVersionComparator implements Comparator<Version> {
    @Override
    public int compare(Version left, Version right) {
      int majorMinorComp = MAJOR_MINOR_COMPARATOR.compare(left, right);
      if (majorMinorComp == 0) {
        return Integer.compare(left.patch, right.patch);
      } else {
        return majorMinorComp;
      }
    }
  }
}
