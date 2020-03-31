package jflex.ucd_generator.scanner;

import static jflex.ucd_generator.util.PropertyNameNormalizer.DEFAULT_CATEGORIES;

import com.google.common.collect.ImmutableSortedMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import jflex.ucd_generator.scanner.model.UnicodeData;
import jflex.ucd_generator.ucd.CodepointRange;
import jflex.ucd_generator.ucd.CodepointRangeSet;
import jflex.ucd_generator.ucd.MutableCodepointRange;
import jflex.ucd_generator.util.PropertyNameNormalizer;

/**
 * Scans ScriptExtensions.txt, using previously parsed Scripts(-X.Y.Z).txt values for missing code
 * points.
 */
public abstract class AbstractScriptExtensionsScanner {

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
        unicodeData.getCanonicalPropertyName(PropertyNameNormalizer.NORMALIZED_SCRIPT);
    String scriptPropertyAliasPrefix = canonicalScriptPropertyName + "=";
    for (SortedMap.Entry<String, String> entry : getUsedPropertyValueAliases().entrySet()) {
      String propertyValueAlias = entry.getKey();
      if (propertyValueAlias.startsWith(scriptPropertyAliasPrefix)) {
        String canonicalScriptValue = entry.getValue();
        scripts.add(canonicalScriptValue);
      }
    }
  }

  void addPropertyValueIntervals() {
    // Add script property value for missing code points.
    // TODO(regisd) why???
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
        unicodeData.addPropertyInterval(propertyName, script, range.start(), range.end());
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
    if (unicodeData.hasUsedEnumeratedProperty(PropertyNameNormalizer.NORMALIZED_GENERAL_CATEGORY)) {
      usedPropertyValueAliases.put(PropertyNameNormalizer.NORMALIZED_GENERAL_CATEGORY, "lc");
    }
    for (String propName : unicodeData.usedEnumeratedProperties().keySet()) {
      Collection<String> propValues = unicodeData.usedEnumeratedProperties().get(propName);
      for (String propValue : propValues) {
        String canonicalValue = PropertyNameNormalizer.canonicalValue(propName, propValue);

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
