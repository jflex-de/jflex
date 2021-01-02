/*
 * Copyright (C) 2009-2013 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2020 Google, LLC.
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
package de.jflex.ucd_generator.scanner;

import de.jflex.ucd_generator.ucd.UnicodeData;
import de.jflex.ucd_generator.util.PropertyNameNormalizer;
import java.util.HashSet;
import java.util.Set;

/** Scanner for {@code PropertyValueAliases(-X.X.X).txt}. */
abstract class AbstractPropertyValueAliasesScanner {

  private final UnicodeData unicodeData;

  protected final Set<String> aliases = new HashSet<>();

  protected String propertyAlias;
  protected String propertyValue;

  public AbstractPropertyValueAliasesScanner(UnicodeData unicodeData) {
    this.unicodeData = unicodeData;
  }

  /** Populates the property values and property value aliases for a property. */
  void addPropertyValueAliases() {
    addPropertyValueAliases(
        unicodeData.getCanonicalPropertyName(propertyAlias),
        PropertyNameNormalizer.normalize(propertyValue));
  }

  private void addPropertyValueAliases(
      String canonicalPropertyName, String normalizedPropertyValue) {
    aliases.add(normalizedPropertyValue);
    unicodeData.addPropertyValueAliases(canonicalPropertyName, normalizedPropertyValue, aliases);
    aliases.clear();
  }
}
