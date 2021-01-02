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

import de.jflex.migration.unicodedatatest.base.AbstractSimpleParser;
import java.io.Reader;
import java.util.regex.Pattern;

/**
 * Parser of unicode {@code Blocks.txt}.
 *
 * <p>Recent Unicode
 *
 * <pre>{@code
 * # Property:	Block
 * #
 * # @missing: 0000..10FFFF; No_Block
 *
 * 0000..007F; Basic Latin
 * 0080..00FF; Latin-1 Supplement
 * }</pre>
 *
 * <p>Archaic unicode
 *
 * <pre>{@code
 * # Start Code; End Code; Block Name
 * 0000; 007F; Basic Latin
 * 0080; 00FF; Latin-1 Supplement
 * }</pre>
 */
public class SimpleBlocksParser extends AbstractSimpleParser {

  private static final Pattern PATTERN =
      Pattern.compile("^([0-9A-F]+)(?:\\.\\.|;\\s*)([0-9A-F]+); (.*)");

  protected SimpleBlocksParser(Reader reader, PatternHandler handler) {
    super(PATTERN, reader, handler);
  }
}
