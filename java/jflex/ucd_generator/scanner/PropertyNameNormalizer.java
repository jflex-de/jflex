package jflex.ucd_generator.scanner;

import java.util.HashMap;
import jflex.ucd_generator.base.Aliases;

public class PropertyNameNormalizer {

  private HashMap<String, String> propertyAlias2CanonicalName = new HashMap<>();

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
    propertyAlias = Aliases.normalize(propertyAlias);
    return propertyAlias2CanonicalName.getOrDefault(propertyAlias, propertyAlias);
  }

  public void putPropertyAlias(String alias, String canonicalName) {
    propertyAlias2CanonicalName.put(alias, canonicalName);
  }
}
