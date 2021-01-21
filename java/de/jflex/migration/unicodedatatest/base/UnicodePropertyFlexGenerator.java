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
package de.jflex.migration.unicodedatatest.base;

import com.google.common.collect.ImmutableSortedMap;
import java.util.NavigableMap;

/** Generates the flex of the scanners for a all ages of a given Unicode version. */
public class UnicodePropertyFlexGenerator<T>
    extends AbstractGenerator<UnicodePropertyFlexTemplateVars<T>> {

  private final String className;
  private final NavigableMap<String, T> properties;
  private final Class<T> propertyValueClass;

  public UnicodePropertyFlexGenerator(
      UnicodeVersion unicodeVersion,
      String className,
      NavigableMap<String, T> properties,
      Class<T> propertyValueClass) {
    super("UnicodeProperty.flex", unicodeVersion);
    this.className = className;
    this.properties = properties;
    this.propertyValueClass = propertyValueClass;
  }

  @Override
  protected String getOuputFileName(UnicodePropertyFlexTemplateVars<T> vars) {
    return vars.className + ".flex";
  }

  @Override
  protected UnicodePropertyFlexTemplateVars<T> createTemplateVars() {
    UnicodePropertyFlexTemplateVars<T> vars = new UnicodePropertyFlexTemplateVars<>();
    vars.className = className;
    vars.properties = properties;
    vars.propertyValueClass = propertyValueClass;
    return vars;
  }

  public static UnicodePropertyFlexGenerator<String> createStringProperty(
      UnicodeVersion version, String className, String propertyName) {
    return new UnicodePropertyFlexGenerator<>(
        version,
        className,
        ImmutableSortedMap.of(propertyName, "\"" + propertyName + "\""),
        String.class);
  }
}
