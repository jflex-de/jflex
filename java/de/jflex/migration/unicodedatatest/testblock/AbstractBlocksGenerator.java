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
import static de.jflex.migration.unicodedatatest.util.JavaResources.readResource;

import com.google.common.collect.ImmutableList;
import com.google.common.flogger.FluentLogger;
import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.migration.unicodedatatest.base.UnicodeVersionTemplateVars;
import de.jflex.testing.unicodedata.BlockSpec;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.Versions;
import de.jflex.util.javac.JavaPackageUtils;
import de.jflex.velocity.Velocity;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.function.Function;
import org.apache.velocity.runtime.parser.ParseException;

abstract class AbstractBlocksGenerator<T extends UnicodeVersionTemplateVars>
    extends AbstractGenerator {
  private static final String ROOT_DIR =
      JavaPackageUtils.getPathForClass(AbstractBlocksGenerator.class);

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private final String templateResource;
  private final String templateName;
  protected final UnicodeVersion unicodeVersion;
  protected final ImmutableList<BlockSpec> blocks;

  public AbstractBlocksGenerator(
      String templateResource,
      String templateName,
      UnicodeVersion unicodeVersion,
      ImmutableList<BlockSpec> blocks) {
    this.templateResource = ROOT_DIR + "/" + templateResource;
    this.templateName = templateName;
    this.unicodeVersion = unicodeVersion;
    if (unicodeVersion.version().equals(Versions.VERSION_2_0)) {
      this.blocks = fixBlocksForUnicode_2_0(blocks);
    } else {
      this.blocks = ImmutableList.copyOf(blocks);
    }
  }

  @Override
  public void generate(Path outDir) throws IOException, ParseException {
    T vars = createTemplateVars();
    vars.updateFrom(unicodeVersion);
    Path outFile = getOuputFilePath(outDir, vars);
    logger.atInfo().log("Generating %s", outFile.toAbsolutePath());
    InputStreamReader templateReader = readResource(templateResource);
    Velocity.render(templateReader, templateName, vars, outFile.toFile());
  }

  /**
   * Fix error in Unicode 2.0 {@code Blocks-1.txt} has an error.
   *
   * <p>Character U+FEFF is assigned to two different blocks. Since the single char in the second
   * range (U+FEFF) is not an Arabic character, but rather the zero-width no-break space char, the
   * FE70..FEFF block should be shortened to exclude this char. This reflects the correction made in
   * all following Unicode versions.
   */
  private static ImmutableList<BlockSpec> fixBlocksForUnicode_2_0(ImmutableList<BlockSpec> blocks) {
    Function<BlockSpec, BlockSpec> maybeFixArabicBlock =
        blockSpec -> {
          if (blockSpec.range().equals(CodepointRange.create(0xFE70, 0xFEFF))) {
            return BlockSpec.create(blockSpec.name(), blockSpec.range().start(), /*end=*/ 0xFEFE);
          } else {
            return blockSpec;
          }
        };
    return blocks.stream().map(maybeFixArabicBlock).collect(toImmutableList());
  }

  protected abstract T createTemplateVars();

  protected abstract Path getOuputFilePath(Path outDir, T vars);
}
