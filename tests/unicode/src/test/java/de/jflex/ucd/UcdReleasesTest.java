/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.jflex.version.Version;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Test for {@link UcdReleases}. Needs the UCD downloads, so this only runs in Maven. */
public class UcdReleasesTest {

  /** A sorted copy, to compare lists without regard to order. */
  private static List<String> sorted(List<String> list) {
    List<String> result = new ArrayList<>(list);
    Collections.sort(result);
    return result;
  }

  private static List<String> fileNames(Version version) throws IOException {
    List<String> names = new ArrayList<>();
    for (String file :
        UcdReleases.ucdFiles(Paths.get(System.getProperty("jflex.ucd.dir")), version)) {
      names.add(Paths.get(file).getFileName().toString());
    }
    return names;
  }

  @Test
  public void versions_haveUpdateNumbers() {
    List<Version> versions = UcdReleases.versions();

    assertEquals("1.1.5", versions.get(0).toString());
    assertEquals("12.1.0", versions.get(versions.size() - 1).toString());
  }

  @Test
  public void ucdFiles_combinesReleases() throws IOException {
    assertEquals(
        sorted(
            Arrays.asList(
                "Blocks-3.txt",
                "LineBreak-5.txt",
                "PropList-3.0.1.txt",
                "UnicodeData-3.0.1.txt",
                "DerivedAge.txt")),
        sorted(fileNames(new Version(3, 0))));
  }

  /** The zip files have more files, and the emoji data is not one of them. */
  @Test
  public void ucdFiles_takesKnownFilesAndEmojiData() throws IOException {
    assertEquals(
        sorted(
            Arrays.asList(
                "Blocks.txt",
                "DerivedAge.txt",
                "DerivedCoreProperties.txt",
                "GraphemeBreakProperty.txt",
                "LineBreak.txt",
                "PropList.txt",
                "PropertyAliases.txt",
                "PropertyValueAliases.txt",
                "ScriptExtensions.txt",
                "Scripts.txt",
                "SentenceBreakProperty.txt",
                "UnicodeData.txt",
                "WordBreakProperty.txt",
                "emoji-data.txt")),
        sorted(fileNames(new Version(12, 1))));
  }

  @Test
  public void ucdFiles_unknownVersion() {
    assertThrows(NullPointerException.class, () -> fileNames(new Version(99, 0)));
  }
}
