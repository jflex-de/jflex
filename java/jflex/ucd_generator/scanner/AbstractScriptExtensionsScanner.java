package jflex.ucd_generator.scanner;

import com.google.common.collect.ImmutableSortedMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import jflex.ucd_generator.scanner.model.PropertyValueIntervals;
import jflex.ucd_generator.scanner.model.UnicodeData;
import jflex.ucd_generator.ucd.CodepointRange;
import jflex.ucd_generator.ucd.CodepointRangeSet;
import jflex.ucd_generator.ucd.MutableCodepointRange;

/**
 * Scans ScriptExtensions.txt, using previously parsed Scripts(-X.Y.Z).txt values for missing code
 * points.
 */
public abstract class AbstractScriptExtensionsScanner {

  private final UnicodeData unicodeData;
  private final Map<String, CodepointRangeSet.Builder> scriptIntervals = new HashMap<>();
  private final Set<String> scripts = new HashSet<>();
  private int start;
  private int end;
  private String propertyName;
  private boolean[] scriptExtensionsCodePoint;

  protected AbstractScriptExtensionsScanner(UnicodeData unicodeData) {
    this.unicodeData = unicodeData;
    scriptExtensionsCodePoint = new boolean[unicodeData.maximumCodePoint + 1];

    // Collect all script property values
    String canonicalScriptPropertyName = unicodeData.getCanonicalPropertyName("script");
    String scriptPropertyAliasPrefix = canonicalScriptPropertyName + "=";
    for (SortedMap.Entry<String, String> entry : getUsedPropertyValueAliases().entrySet()) {
      String propertyValueAlias = entry.getKey();
      if (propertyValueAlias.startsWith(scriptPropertyAliasPrefix)) {
        String canonicalScriptValue = entry.getValue();
        scripts.add(canonicalScriptValue);
      }
    }
  }

  private void addPropertyValueIntervals() {
    // Add script property value for missing code points
    PropertyValueIntervals propertyValueIntervals = unicodeData.propertyValueIntervals;
    for (String script : scripts) {
      CodepointRangeSet.Builder intervalsBuilder =
          scriptIntervals.computeIfAbsent(script, k -> CodepointRangeSet.builder());
      for (MutableCodepointRange range : propertyValueIntervals.getRanges(script)) {
        for (int ch = range.start; ch <= range.end; ++ch) {
          if (!scriptExtensionsCodePoint[ch]) {
            intervalsBuilder.add(new MutableCodepointRange(ch, ch));
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
    ImmutableSortedMap.Builder<String, String> usedPropertyValues =
        ImmutableSortedMap.naturalOrder();
    for (String binaryProperty : unicodeData.usedBinaryProperties()) {
      for (String nameAlias : unicodeData.getPropertyAliases(binaryProperty)) {
        if (!Objects.equals(nameAlias, binaryProperty)) {
          unicodeData.addUsedPropertyValueAlias(nameAlias, binaryProperty);
        }
      }
    }
    Set<String> genCatProps = usedEnumeratedProperties.get(NORMALIZED_GENERAL_CATEGORY);
    if (null != genCatProps) {
      genCatProps.add("lc");
    }
    for (Map.Entry<String, Set<String>> entry : usedEnumeratedProperties.entrySet()) {
      String propName = entry.getKey();
      Set<String> propValues = entry.getValue();
      for (String propValue : propValues) {
        String canonicalValue = propName + '=' + propValue;

        // Add value-only aliases for General Category and Script properties.
        if (Objects.equals(propName, NORMALIZED_SCRIPT)
            || Objects.equals(propName, NORMALIZED_GENERAL_CATEGORY)) {
          canonicalValue = propValue;
          for (String valueAlias : getPropertyValueAliases(propName, propValue)) {
            if (!Objects.equals(valueAlias, propValue)) {
              usedPropertyValueAliases.put(valueAlias, propValue);
            }
          }
        }
        for (String nameAlias : getPropertyAliases(propName)) {
          for (String valueAlias : getPropertyValueAliases(propName, propValue)) {
            // Both property names and values have self-aliases; when generating
            // all possible alias combinations, exclude the one that is the same
            // as the full property name + full property value, unless the
            // property is General Category or Script.
            if (Objects.equals(propName, NORMALIZED_SCRIPT)
                || Objects.equals(propName, NORMALIZED_GENERAL_CATEGORY)
                || !(Objects.equals(nameAlias, propName)
                    && Objects.equals(valueAlias, propValue))) {
              String alias = nameAlias + '=' + valueAlias;
              usedPropertyValueAliases.put(alias, canonicalValue);
            }
          }
        }
      }
    }
    return usedPropertyValues.build();
  }
}
