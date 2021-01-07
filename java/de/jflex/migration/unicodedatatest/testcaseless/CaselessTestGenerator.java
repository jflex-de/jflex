/*
 * Copyright (C) 2021 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.jflex.migration.unicodedatatest.testcaseless;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.testing.unicodedata.AbstractSimpleParser.PatternHandler;
import de.jflex.testing.unicodedata.SimpleCaselessParser;
import de.jflex.ucd.UcdFileType;
import de.jflex.ucd.UcdVersion;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.velocity.runtime.parser.ParseException;

public class CaselessTestGenerator {

  private CaselessTestGenerator() {}

  public static void main(String[] args) throws IOException, ParseException {
    UnicodeVersion version = UnicodeVersion.create(args[0]);
    Path outDir = Paths.get(args[1]);
    List<String> files = Arrays.asList(Arrays.copyOfRange(args, 2, args.length));
    UcdVersion ucd = UcdVersion.findUcdFiles(version.version(), files);
    Path ucdUnicodeData = ucd.getFile(UcdFileType.UnicodeData).toPath();
    Equivalences<Integer> equivalences = parseUnicodeData(ucdUnicodeData);
    if (equivalences.getKeys().isEmpty()) {
      throw new IllegalStateException("No equivalence found in " + ucdUnicodeData);
    }
    generate(version, outDir, equivalences);
  }

  static Equivalences<Integer> parseUnicodeData(Path ucdUnicodeData) throws IOException {
    CaselessHandler handler = new CaselessHandler();
    SimpleCaselessParser parser =
        new SimpleCaselessParser(
            Files.newBufferedReader(ucdUnicodeData, StandardCharsets.UTF_8), handler);
    parser.parse();
    return handler.equivalences;
  }

  private static void generate(
      UnicodeVersion version, Path outDir, Equivalences<Integer> equivalences)
      throws IOException, ParseException {
    new UnicodeCaselessFlexGenerator(version, equivalences).generate(outDir);
    new UnicodeCaselessTestGenerator(version).generate(outDir);
    new UnicodeCaselessGoldenGenerator(version, equivalences).generate(outDir);
  }

  private static class CaselessHandler implements PatternHandler {
    Equivalences<Integer> equivalences = new Equivalences<>();

    @Override
    public void onRegexMatch(List<String> regexpGroups) {
      String strUpperCaseMapping = regexpGroups.get(1);
      String strLowerCaseMapping = regexpGroups.get(2);
      String strTitleCaseMapping = regexpGroups.get(3);
      if (strUpperCaseMapping.isEmpty()
          && strLowerCaseMapping.isEmpty()
          && strTitleCaseMapping.isEmpty()) {
        return;
      }
      int codePoint = Integer.parseInt(regexpGroups.get(0), 16);
      maybeAddMapping(codePoint, strLowerCaseMapping);
      maybeAddMapping(codePoint, strUpperCaseMapping);
      maybeAddMapping(codePoint, strTitleCaseMapping);
      // TODO(regisd)
    }

    private void maybeAddMapping(int codepoint, String strMapping) {
      if (!strMapping.isEmpty()) {
        int mapping = Integer.parseInt(strMapping, 16);
        equivalences.add(codepoint, mapping);
      }
    }
  }

  static class Equivalences<T extends Comparable<T>> {
    /** Mapping from value → equivalent values. */
    protected Map<T, Set<T>> equivalences = new HashMap<>();

    Set<T> get(T value) {
      return equivalences.computeIfAbsent(
          value,
          v -> {
            Set<T> set = new HashSet<>();
            set.add(value);
            return set;
          });
    }

    void add(T codepoint, T mapping) {
      if (equivalences.containsKey(mapping)) {
        Set<T> equiv = equivalences.get(mapping);
        equiv.add(codepoint);
        equivalences.put(codepoint, equiv);
      } else {
        Set<T> equiv = get(codepoint);
        equiv.add(mapping);
        equivalences.put(mapping, equiv);
      }
    }

    Set<T> getKeys() {
      return equivalences.keySet();
    }

    ImmutableList<T> getSortedKeys(Comparator<T> comparator) {
      return ImmutableList.sortedCopyOf(comparator, getKeys());
    }

    /**
     * Returns the equivalent value of the given value, i.e. the minimum value in the equivalence
     * set.
     */
    public T getEquivalentValue(T value) {
      return Collections.min(equivalences.get(value));
    }
  }
}
