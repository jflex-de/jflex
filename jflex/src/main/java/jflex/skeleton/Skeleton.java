/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jflex.skeleton;

import static java.nio.charset.StandardCharsets.UTF_8;
import static jflex.logging.Out.NL;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import jflex.exceptions.GeneratorException;
import jflex.l10n.ErrorMessages;
import jflex.logging.Out;

/**
 * The skeleton of generated scanners.
 *
 * <p>The skeleton consists of several sections that are emitted to a file. Usually there is a
 * portion of generated code (produced in class Emitter) between every two sections of skeleton
 * code.
 *
 * <p>Instances are immutable. Functions like {@link #makePrivate()} that make changes return a new
 * skeleton.
 *
 * @see jflex.generator.Emitter
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 */
public final class Skeleton {

  /** location of default skeleton */
  private static final String DEFAULT_LOC = "jflex/skeleton.default";

  /** expected number of sections in the skeleton file */
  private static final int SIZE = 21;

  /** The default skeleton. Looks in the current system class path. */
  public static final Skeleton DEFAULT = readDefault();

  /** The sections of the skeleton */
  private final String[] sections;

  private Skeleton(String[] sections) {
    this.sections = sections;
  }

  /** Returns the number of sections. */
  public int size() {
    return sections.length;
  }

  /**
   * Returns a section of the skeleton.
   *
   * @param i the number of the section, {@code 0 <= i < size()}
   */
  public String section(int i) {
    return sections[i];
  }

  /**
   * Returns version of this skeleton where all public declarations are replaced with private.
   *
   * <p>Replaces all occurrences of " public " in the skeleton with " private ".
   */
  public Skeleton makePrivate() {
    String[] result = new String[sections.length];
    for (int i = 0; i < sections.length; i++) {
      result[i] = replace(" public ", " private ", sections[i]);
    }
    return new Skeleton(result);
  }

  /**
   * Reads an external skeleton file.
   *
   * @param skeletonFile the file to read (must be != null and readable)
   * @throws GeneratorException if the file cannot be read or is not a skeleton
   */
  public static Skeleton readSkelFile(File skeletonFile) {
    if (skeletonFile == null) throw new IllegalArgumentException("Skeleton file must not be null");

    if (!skeletonFile.isFile() || !skeletonFile.canRead()) {
      Out.error(ErrorMessages.CANNOT_READ_SKEL, skeletonFile.toString());
      throw new GeneratorException();
    }

    Out.println(ErrorMessages.READING_SKEL, skeletonFile.toString());

    try (BufferedReader reader =
        Files.newBufferedReader(Paths.get(skeletonFile.toString()), UTF_8)) {
      return readSkel(reader);
    } catch (IOException e) {
      Out.error(ErrorMessages.SKEL_IO_ERROR);
      throw new GeneratorException(e);
    }
  }

  /**
   * Reads an external skeleton file from a BufferedReader.
   *
   * @param reader the reader to read from (must be != null)
   * @throws java.io.IOException if an IO error occurs
   * @throws GeneratorException if the number of skeleton sections does not match
   */
  public static Skeleton readSkel(BufferedReader reader) throws IOException {
    List<String> lines = new ArrayList<>();
    StringBuilder section = new StringBuilder();

    String ln;
    while ((ln = reader.readLine()) != null) {
      if (ln.startsWith("---")) { // $NON-NLS-1$
        lines.add(section.toString());
        section.setLength(0);
      } else {
        section.append(ln);
        section.append(NL);
      }
    }

    if (section.length() > 0) lines.add(section.toString());

    if (lines.size() != SIZE) {
      Out.error(ErrorMessages.WRONG_SKELETON);
      throw new GeneratorException();
    }

    return new Skeleton(lines.toArray(new String[0]));
  }

  /**
   * Replaces a with b in c.
   *
   * @param a the String to be replaced
   * @param b the replacement
   * @param c the String in which to replace a by b
   * @return a String object with a replaced by b in c
   */
  public static String replace(String a, String b, String c) {
    StringBuilder result = new StringBuilder(c.length());
    int i = 0;
    int j = c.indexOf(a);

    while (j >= i) {
      result.append(c.substring(i, j));
      result.append(b);
      i = j + a.length();
      j = c.indexOf(a, i);
    }

    result.append(c.substring(i, c.length()));

    return result.toString();
  }

  /** Loads the default skeleton. Looks in the current system class path. */
  private static Skeleton readDefault() {
    ClassLoader l = Skeleton.class.getClassLoader();
    URL url;

    /* Try to load from same class loader as this class.
     * Should work, but does not on OS/2 JDK 1.1.8 (see bug 1065521).
     * Use system class loader in this case.
     */
    if (l != null) {
      url = l.getResource(DEFAULT_LOC);
    } else {
      url = ClassLoader.getSystemResource(DEFAULT_LOC);
    }

    if (url == null) {
      Out.error(ErrorMessages.SKEL_IO_ERROR_DEFAULT);
      throw new GeneratorException();
    }

    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(url.openStream(), UTF_8))) {
      return readSkel(reader);
    } catch (IOException e) {
      Out.error(ErrorMessages.SKEL_IO_ERROR_DEFAULT);
      throw new GeneratorException(e);
    }
  }
}
