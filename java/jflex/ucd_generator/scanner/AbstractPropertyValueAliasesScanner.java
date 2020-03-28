package jflex.ucd_generator.scanner;

import java.util.HashSet;
import java.util.Set;
import jflex.ucd_generator.scanner.model.UnicodeData;
import jflex.ucd_generator.util.PropertyNameNormalizer;

/** Scanner for {@code PropertyValueAliases(-X.X.X).txt}. */
public abstract class AbstractPropertyValueAliasesScanner {

  private final UnicodeData unicodeData;

  // TODO(regisd) Clone Script/sc property value aliases => Script_Extensions/scx
  @SuppressWarnings("unused")
  final String scxPropName;

  protected final Set<String> aliases = new HashSet<>();

  protected String propertyAlias;
  protected String propertyValue;

  public AbstractPropertyValueAliasesScanner(UnicodeData unicodeData) {
    this.unicodeData = unicodeData;
    scxPropName = unicodeData.getCanonicalPropertyName("Script_Extensions");
  }

  /** Populates the property values and property value aliases for a property. */
  void addPropertyValueAliases() {
    addPropertyValueAliases(
        unicodeData.getCanonicalPropertyName(propertyAlias),
        PropertyNameNormalizer.normalize(propertyValue));
  }

  private void addPropertyValueAliases(String propertyName, String normalizedPropertyValue) {
    aliases.add(normalizedPropertyValue);
    unicodeData.addPropertyValueAliases(propertyName, normalizedPropertyValue, aliases);
    aliases.clear();
  }
}
