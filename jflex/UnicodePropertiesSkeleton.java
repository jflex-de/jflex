/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.5                                                               *
 * Copyright (C) 1998-2013  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;


/**
 * This class stores the skeleton of generated UnicodeProperties.java
 *
 * The skeleton consists of several parts that can be emitted to
 * a file. Usually there is a portion of generated code between every
 * two parts of skeleton code.
 *
 * There is a static part (the skeleton code) and state based iterator
 * part to this class. The iterator part is used to emit consecutive skeleton
 * sections to some <code>StringBuilder</code>.
 *
 * Copied from the version used for scanner generation.
 */
public class UnicodePropertiesSkeleton {

  /** expected number of sections in the skeleton file */
  static final private int size = 5;

  /** platform specific newline */
  static final private String NL = System.getProperty("line.separator");

  /** The skeleton */
  private List<String> sections = new ArrayList<String>();

  /** The next element of the skeleton to be emitted */
  private int pos = 0;


  /**
   * Creates a new skeleton (iterator) instance.
   *
   * @param skeletonFilename The name of the skeleton file to use
   * @throws Exception If there is a problem locating or reading in the skeleton
   *  file
   */
  public UnicodePropertiesSkeleton(String skeletonFilename) throws Exception {
    read(skeletonFilename);
  }


  /**
   * Emits the next part of the skeleton
   *
   * @param out Where to emit the next part of the skeleton
   */
  public void emitNext(StringBuilder out) {
    out.append(sections.get(pos++));
  }


  /**
   * Reads an external skeleton file from a BufferedReader.
   *
   * @param  skeletonFilename The name of the skeleton file
   * @throws Exception if an IO error occurs
   */
  public void read(String skeletonFilename) throws Exception {

    ClassLoader loader = UnicodePropertiesSkeleton.class.getClassLoader();
    URL url = loader.getResource(skeletonFilename);

    if (null == url) {
      throw new Exception("Cannot locate '" + skeletonFilename
                          + "' - aborting.");
    }
    String line;
    StringBuilder section = new StringBuilder();
    BufferedReader reader = new BufferedReader
      (new InputStreamReader(url.openStream(), "UTF-8"));
    while (null != (line = reader.readLine())) {
      if (line.startsWith("---")) {
        sections.add(section.toString());
        section.setLength(0);
      } else {
        section.append(line);
        section.append(NL);
      }
    }
    if (section.length() > 0) {
      sections.add(section.toString());
    }
    if (sections.size() != size) {
      throw new Exception("Skeleton file '" + skeletonFilename + "' has "
                          + sections.size() + " static sections, but " + size
                          + " were expected.");
    }
  }
}