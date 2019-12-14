package jflex.ucd_generator.scanner;

import java.util.HashSet;
import java.util.Set;

/** Scanner for {@code PropertyValueAliases(-X.X.X).txt}. */
public class AbstractPropertyValueAliasesScanner {

  private final UnicodeData.Builder unicodeDataBuilder;
  private final String scxPropName;

  protected final Set<String> aliases = new HashSet<>();

  protected String propertyAlias;
  protected String propertyValue;

  public AbstractPropertyValueAliasesScanner(UnicodeData.Builder unicodeDataBuilder) {
    this.unicodeDataBuilder = unicodeDataBuilder;
    scxPropName = unicodeDataBuilder.getCanonicalPropertyName("Script_Extensions");
  }

  /** Populates the property values and property value aliases for a property. */
  void addPropertyValueAliases() {
    addPropertyValueAliases(
        unicodeDataBuilder.getCanonicalPropertyName(propertyAlias),
        PropertyNameNormalizer.normalize(propertyValue));
  }

  private void addPropertyValueAliases(String propertyName, String normalizedPropertyValue) {
    aliases.add(normalizedPropertyValue);

    unicodeDataBuilder.addAllPropertyValueAliases(propertyName, aliases, propertyValue);
  }
}
