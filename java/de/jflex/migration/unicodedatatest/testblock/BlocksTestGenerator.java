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
package de.jflex.migration.unicodedatatest.testblock;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.testing.unicodedata.SimpleIntervalsParser;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.NamedCodePointRanges;
import de.jflex.ucd.NamedCodepointRange;
import de.jflex.ucd.UcdFileType;
import de.jflex.ucd.UcdVersion;
import de.jflex.ucd.Versions;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.apache.velocity.runtime.parser.ParseException;

public class BlocksTestGenerator {

  private BlocksTestGenerator() {}

  public static void main(String[] args) throws IOException, ParseException {
    UnicodeVersion version = UnicodeVersion.create(args[0]);
    Path outDir = Paths.get(args[1]);
    List<String> files = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));
    UcdVersion ucd = UcdVersion.findUcdFiles(version.version(), files);
    Path ucdBlocks = ucd.getFile(UcdFileType.Blocks).toPath();
    ImmutableList<NamedCodepointRange<String>> blocks =
        SimpleIntervalsParser.parseUnicodeBlocks(ucdBlocks).stream()
            .filter(b -> !b.isSurrogate())
            .collect(toImmutableList());
    if (version.version().equals(Versions.VERSION_2_0)) {
      blocks = fixBlocksForUnicode_2_0(blocks);
    }
    checkState(!blocks.isEmpty(), "There are no blocks defined in %s", version);
    blocks = NamedCodePointRanges.merge(blocks);
    generate(version, outDir, blocks);
  }

  /**
   * Fix error in Unicode 2.0 {@code Blocks-1.txt} has an error.
   *
   * <p>Character U+FEFF is incorrectly assigned to two different blocks.
   *
   * <pre>{@code
   * FE70; FEFF; Arabic Presentation Forms-B
   * [...]
   * FEFF; FEFF; Specials
   * FFF0; FFFF; Specials
   * }</pre>
   *
   * <p>See https://github.com/jflex-de/jflex/issues/835
   *
   * @see de.jflex.ucd_generator.ucd.UnicodeData#hackUnicode_2_0
   */
  private static ImmutableList<NamedCodepointRange<String>> fixBlocksForUnicode_2_0(
      ImmutableList<NamedCodepointRange<String>> blocks) {
    ImmutableList.Builder<NamedCodepointRange<String>> fixedBlocks = ImmutableList.builder();
    CodepointRange arabicRange = CodepointRange.create(0xFE70, 0xFEFF);
    CodepointRange lastSpecials = CodepointRange.create(0xFFF0, 0xFFFF);
    for (NamedCodepointRange<String> block : blocks) {
      if (block.range().equals(arabicRange)) {
        fixedBlocks.add(
            NamedCodepointRange.create(block.name(), /*start=*/ 0xFE70, /*end=*/ 0xFEFE));
      } else if (block.range().equals(lastSpecials)) {
        fixedBlocks.add(NamedCodepointRange.create(block.name(), 0xFFF0, 0xFFFD));
      } else {
        fixedBlocks.add(block);
      }
    }
    return fixedBlocks.build();
  }

  private static void generate(
      UnicodeVersion version, Path outDir, ImmutableList<NamedCodepointRange<String>> blocks)
      throws IOException, ParseException {
    UnicodeBlockFlexGenerator.create(version, blocks).generate(outDir);
    new UnicodeBlocksTestJavaGenerator(version, blocks).generate(outDir);
  }
}
