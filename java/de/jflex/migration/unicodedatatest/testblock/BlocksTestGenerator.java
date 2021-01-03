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

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.AbstractSimpleParser.PatternHandler;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.testing.unicodedata.BlockSpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.apache.velocity.runtime.parser.ParseException;

public class BlocksTestGenerator {

  private BlocksTestGenerator() {}

  public static void main(String[] args) throws IOException, ParseException {
    UnicodeVersion version = UnicodeVersion.create(args[0]);
    Path outDir = Paths.get(args[1]);
    Path ucdBlocks = Paths.get(args[2]);
    ImmutableList<BlockSpec> blocks =
        parseUnicodeBlock(ucdBlocks).stream()
            .filter(b -> !b.isSurrogate())
            .collect(toImmutableList());
    generate(version, outDir, blocks);
  }

  private static ImmutableList<BlockSpec> parseUnicodeBlock(Path ucdBlocks) throws IOException {
    ImmutableList.Builder<BlockSpec> list = ImmutableList.builder();
    PatternHandler handler = regexpGroups -> list.add(createBlock(regexpGroups));
    SimpleBlocksParser parser =
        new SimpleBlocksParser(Files.newBufferedReader(ucdBlocks, StandardCharsets.UTF_8), handler);
    parser.parse();
    return list.build();
  }

  private static BlockSpec createBlock(List<String> regexpGroups) {
    return BlockSpec.create(
        regexpGroups.get(2),
        Integer.parseInt(regexpGroups.get(0), 16),
        Integer.parseInt(regexpGroups.get(1), 16));
  }

  private static void generate(UnicodeVersion version, Path outDir, ImmutableList<BlockSpec> blocks)
      throws IOException, ParseException {
    Path outDirectory = outDir.resolve("javatests").resolve(version.javaPackageDirectory());
    new UnicodeBlockFlexGenerator(version, blocks).generate(outDirectory);
    new UnicodeBlocksTestJavaGenerator(version, blocks).generate(outDirectory);
  }
}
