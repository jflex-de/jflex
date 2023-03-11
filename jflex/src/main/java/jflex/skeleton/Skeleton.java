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
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import jflex.exceptions.GeneratorException;
import jflex.l10n.ErrorMessages;
import jflex.logging.Out;

/**
 * This class stores the skeleton of generated scanners.
 *
 * <p>The skeleton consists of several parts that can be emitted to a file. Usually there is a
 * portion of generated code (produced in class Emitter) between every two parts of skeleton code.
 *
 * <p>There is a static part (the skeleton code) and state based iterator part to this class. The
 * iterator part is used to emit consecutive skeleton sections to some {@code PrintWriter}.
 *
 * @see jflex.generator.Emitter
 * @author Gerwin Klein
 * @version JFlex 1.9.1
 */
public class Skeleton {

  /** location of default skeleton */
  private static final String DEFAULT_LOC = "jflex/skeleton.default";

  /** expected number of sections in the skeleton file */
  private static final int size = 21;

  /** The skeleton */
  public static String[] line;

  static {
    readDefault();
  }

  // the state based, iterator part of Skeleton:

  /** The current part of the skeleton (an index of nextStop[]) */
  private int pos;

  /** The writer to write the skeleton-parts to */
  private final PrintWriter out;

  /**
   * Creates a new skeleton (iterator) instance.
   *
   * @param out the writer to write the skeleton-parts to
   */
  public Skeleton(PrintWriter out) {
    this.out = out;
  }

  /** Emits the next part of the skeleton */
  public void emitNext() {
    out.print(line[pos++]);
  }

  /**
   * Make the skeleton private.
   *
   * <p>Replaces all occurrences of " public " in the skeleton with " private ".
   */
  public static void makePrivate() {
    for (int i = 0; i < line.length; i++) {
      line[i] = replace(" public ", " private ", line[i]);
    }
  }

  /**
   * Reads an external skeleton file for later use with this class.
   *
   * @param skeletonFile the file to read (must be != null and readable)
   */
  public static void readSkelFile(File skeletonFile) {
    if (skeletonFile == null) throw new IllegalArgumentException("Skeleton file must not be null");

    if (!skeletonFile.isFile() || !skeletonFile.canRead()) {
      Out.error(ErrorMessages.CANNOT_READ_SKEL, skeletonFile.toString());
      throw new GeneratorException();
    }

    Out.println(ErrorMessages.READING_SKEL, skeletonFile.toString());

    try (BufferedReader reader =
        Files.newBufferedReader(Paths.get(skeletonFile.toString()), UTF_8)) {
      readSkel(reader);
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
  public static void readSkel(BufferedReader reader) throws IOException {
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

    if (lines.size() != size) {
      Out.error(ErrorMessages.WRONG_SKELETON);
      throw new GeneratorException();
    }

    line = new String[size];
    for (int i = 0; i < size; i++) line[i] = lines.get(i);
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

  /** (Re)load the default skeleton. Looks in the current system class path. */
  public static void readDefault() {
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
      readSkel(reader);
    } catch (IOException e) {
      Out.error(ErrorMessages.SKEL_IO_ERROR_DEFAULT);
      throw new GeneratorException(e);
    }
  }
}
