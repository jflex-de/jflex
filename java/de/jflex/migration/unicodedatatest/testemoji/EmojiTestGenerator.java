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

package de.jflex.migration.unicodedatatest.testemoji;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import de.jflex.migration.unicodedatatest.base.UnicodePropertyFlexGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.ucd.UcdVersion;
import de.jflex.ucd_generator.scanner.UcdScanner;
import de.jflex.ucd_generator.scanner.UcdScannerException;
import de.jflex.ucd_generator.ucd.UnicodeData;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.apache.velocity.runtime.parser.ParseException;

public class EmojiTestGenerator {

  public static final ImmutableSet<String> EMOJI_PROPERTIES =
      ImmutableSet.of("Emoji", "Emoji_Modifier", "Emoji_Modifier_Base", "Emoji_Presentation");

  private EmojiTestGenerator() {}

  public static void main(String[] args) throws Exception {
    UnicodeVersion version = UnicodeVersion.create(args[0]);
    Path outDir = Paths.get(args[1]);
    UcdVersion ucdVersion =
        UcdVersion.findUcdFiles(
            version.version(), ImmutableList.copyOf(Arrays.copyOfRange(args, 2, args.length)));
    UnicodeData unicodeData = parseUcd(ucdVersion);
    generate(version, unicodeData, outDir);
  }

  private static void generate(UnicodeVersion version, UnicodeData unicodeData, Path outDir)
      throws IOException, ParseException {
    ImmutableSet<String> emojiProperties = propertiesForVersion(version);
    new UnicodeEmojiTestGenerator(version, emojiProperties).generate(outDir);
    for (String propName : emojiProperties) {
      UnicodePropertyFlexGenerator.createPropertyScanner(
              version, "UnicodeEmoji_" + propName + "_" + version.underscoreVersion(), propName)
          .generate(outDir);
      new UnicodeEmojiGoldenGenerator(version, unicodeData, propName).generate(outDir);
    }
  }

  private static UnicodeData parseUcd(UcdVersion ucdVersion) throws UcdScannerException {
    UcdScanner scanner = new UcdScanner(ucdVersion);
    return scanner.scan();
  }

  public static ImmutableSet<String> propertiesForVersion(UnicodeVersion unicodeVersion) {
    ImmutableSet.Builder<String> properties =
        ImmutableSet.<String>builder().addAll(EMOJI_PROPERTIES);
    if (unicodeVersion.version().major >= 11) {
      properties.add("Extended_Pictographic");
    }
    if (unicodeVersion.version().major >= 10) {
      properties.add("Emoji_Component");
    }
    return properties.build();
  }
}
