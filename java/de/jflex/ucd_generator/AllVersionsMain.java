/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator;

import de.jflex.ucd.UcdReleases;
import de.jflex.version.Version;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs {@link Main} for all Unicode versions, see {@code scripts/gen-unicode-tables.sh}.
 *
 * <p>Arguments: the directory with the UCD downloads of {@code javatests/pom.xml}, and the output
 * directory.
 */
public final class AllVersionsMain {

  public static void main(String[] args) throws Exception {
    Path ucdDir = Paths.get(args[0]);
    List<String> argv = new ArrayList<>();
    for (Version version : UcdReleases.versions()) {
      argv.add(Main.ARG_VERSION + version);
      argv.addAll(UcdReleases.ucdFiles(ucdDir, version));
    }
    argv.add(Main.ARG_OUT + args[1]);
    Main.main(argv.toArray(new String[0]));
  }

  private AllVersionsMain() {}
}
