/*
 * Copyright (C) 2014-2017 Steve Rowe <sarowe@gmail.com>
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

import static de.jflex.ucd_generator.ucd.PropertyNames.DEFAULT_CATEGORIES;
import static de.jflex.ucd_generator.ucd.PropertyNames.NORMALIZED_GENERAL_CATEGORY;

import com.google.common.collect.ImmutableSortedMap;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd_generator.ucd.CodepointRangeSet;
import de.jflex.ucd_generator.ucd.MutableCodepointRange;
import de.jflex.ucd_generator.ucd.PropertyNames;
import de.jflex.ucd_generator.ucd.UnicodeData;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Set;

/**
 * Scans ScriptExtensions.txt, using previously parsed Scripts(-X.Y.Z).txt values for missing code
 * points.
 */
abstract class AbstractScriptExtensionsScanner {

  private final UnicodeData unicodeData;
  private final Map<String, CodepointRangeSet.Builder> scriptIntervals = new HashMap<>();
  private final Set<String> scripts = new HashSet<>();
  int start;
  int end;
  String propertyName;
  boolean[] scriptExtensionsCodePoint;

  protected AbstractScriptExtensionsScanner(UnicodeData unicodeData) {
    this.unicodeData = unicodeData;
    scriptExtensionsCodePoint = new boolean[unicodeData.maximumCodePoint() + 1];

    // Collect all script property values
    String canonicalScriptPropertyName =
        unicodeData.getCanonicalPropertyName(PropertyNames.NORMALIZED_SCRIPT);
    String scriptPropertyAliasPrefix = canonicalScriptPropertyName + "=";
    for (NavigableMap.Entry<String, String> entry : getUsedPropertyValueAliases().entrySet()) {
      String propertyValueAlias = entry.getKey();
      if (propertyValueAlias.startsWith(scriptPropertyAliasPrefix)) {
        String canonicalScriptValue = entry.getValue();
        scripts.add(canonicalScriptValue);
      }
    }
  }

  void addPropertyValueIntervals() {
    // Add script property value for missing code points.
    for (String script : scripts) {
      CodepointRangeSet.Builder intervalsBuilder =
          scriptIntervals.computeIfAbsent(script, k -> CodepointRangeSet.builder());
      for (CodepointRange range : unicodeData.getPropertyValueIntervals(script)) {
        for (int ch = range.start(); ch <= range.end(); ++ch) {
          if (!scriptExtensionsCodePoint[ch]) {
            // TODO(regisd) This is very frequent an inefficient
            intervalsBuilder.add(MutableCodepointRange.create(ch, ch));
          }
        }
      }
    }
    for (Map.Entry<String, CodepointRangeSet.Builder> entry : scriptIntervals.entrySet()) {
      String script = entry.getKey();
      CodepointRangeSet intervals = entry.getValue().build();
      for (CodepointRange range : intervals.ranges()) {
        unicodeData.addEnumPropertyInterval(propertyName, script, range.start(), range.end());
      }
    }
  }

  private ImmutableSortedMap<String, String> getUsedPropertyValueAliases() {
    ImmutableSortedMap.Builder<String, String> usedPropertyValueAliases =
        ImmutableSortedMap.naturalOrder();
    for (String binaryProperty : unicodeData.usedBinaryProperties()) {
      for (String nameAlias : unicodeData.getPropertyAliases(binaryProperty)) {
        if (!Objects.equals(nameAlias, binaryProperty)) {
          usedPropertyValueAliases.put(nameAlias, binaryProperty);
        }
      }
    }
    if (unicodeData.hasUsedEnumeratedProperty(NORMALIZED_GENERAL_CATEGORY)) {
      usedPropertyValueAliases.put(NORMALIZED_GENERAL_CATEGORY, "lc");
    }
    for (String propName : unicodeData.usedEnumeratedProperties().keySet()) {
      Collection<String> propValues = unicodeData.usedEnumeratedProperties().get(propName);
      for (String propValue : propValues) {
        String canonicalValue = PropertyNames.canonicalValue(propName, propValue);

        // Add value-only aliases for General Category and Script properties.
        if (DEFAULT_CATEGORIES.contains(propName)) {
          for (String valueAlias : unicodeData.getPropertyValueAliases(propName, propValue)) {
            if (!Objects.equals(valueAlias, propValue)) {
              usedPropertyValueAliases.put(valueAlias, propValue);
            }
          }
        }
        for (String nameAlias : unicodeData.getPropertyAliases(propName)) {
          for (String valueAlias : unicodeData.getPropertyValueAliases(propName, propValue)) {
            // Both property names and values have self-aliases; when generating
            // all possible alias combinations, exclude the one that is the same
            // as the full property name + full property value, unless the
            // property is General Category or Script.
            if (DEFAULT_CATEGORIES.contains(propName)
                || !(Objects.equals(nameAlias, propName)
                    && Objects.equals(valueAlias, propValue))) {
              String alias = nameAlias + '=' + valueAlias;
              usedPropertyValueAliases.put(alias, canonicalValue);
            }
          }
        }
      }
    }
    return usedPropertyValueAliases.build();
  }

  void addScript(String script) {
    CodepointRangeSet.Builder intervals =
        scriptIntervals.computeIfAbsent(
            unicodeData.getCanonicalPropertyValueName("script", script),
            k -> CodepointRangeSet.builder());
    intervals.add(MutableCodepointRange.create(start, end));

    for (int ch = start; ch <= end; ++ch) {
      scriptExtensionsCodePoint[ch] = true;
    }
  }
}
