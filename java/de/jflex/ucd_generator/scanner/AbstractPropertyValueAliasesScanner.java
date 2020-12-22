package de.jflex.ucd_generator.scanner;

import de.jflex.ucd_generator.model.UnicodeData;
import de.jflex.ucd_generator.util.PropertyNameNormalizer;
import java.util.HashSet;
import java.util.Set;

/** Scanner for {@code PropertyValueAliases(-X.X.X).txt}. */
public abstract class AbstractPropertyValueAliasesScanner {

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
