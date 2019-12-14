package jflex.ucd_generator.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class PropertyNameNormalizer {

  /** Pattern used to normalize property value identifiers */
  private static final Pattern WORD_SEP_PATTERN = Pattern.compile("[-_\\s()]");

  private final Map<String, String> propertyAlias2CanonicalName = new HashMap<>();
  private final Map<String, String> propertyValueAlias2CanonicalName = new HashMap<>();

  /**
   * Transforms mixed case identifiers containing spaces, hyphens, and/or underscores by downcasing
   * and removing all spaces, hyphens, underscores, and parentheses; also, converts property
   * name/value separator ':' to '='.
   *
   * @param identifier The identifier to transform
   * @return The transformed identifier
   */
  public static String normalize(String identifier) {
    if (identifier == null) {
      return null;
    }
    return WORD_SEP_PATTERN
        .matcher(identifier.toLowerCase(Locale.ENGLISH))
        .replaceAll("")
        .replace(':', '=');
  }

  /**
   * For the given property name or alias, returns the canonical property name. If none has been
   * encountered, then the given propertyAlias itself is returned.
   *
   * @param propertyAlias The property name or alias for which to lookup the canonical property
   *     name.
   * @return the canonical property name for the given property name or alias. If none has been
   *     encountered, then the given propertyAlias is returned.
   */
  public String getCanonicalPropertyName(String propertyAlias) {
    propertyAlias = normalize(propertyAlias);
    return propertyAlias2CanonicalName.getOrDefault(propertyAlias, propertyAlias);
  }

  public void putPropertyAlias(String alias, String canonicalName) {
    propertyAlias2CanonicalName.put(alias, canonicalName);
  }

  public String getCanonicalPropertyValueName(String propertyAlias) {
    propertyAlias = normalize(propertyAlias);
    return propertyValueAlias2CanonicalName.getOrDefault(propertyAlias, propertyAlias);
  }
}
