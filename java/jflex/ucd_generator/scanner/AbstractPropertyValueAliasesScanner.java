package jflex.ucd_generator.scanner;

import java.util.HashSet;
import java.util.Set;
import jflex.ucd_generator.base.Aliases;

/** Scanner for {@code PropertyValueAliases(-X.X.X).txt}. */
public class AbstractPropertyValueAliasesScanner {

  final Set<String> aliases = new HashSet<>();

  final UnicodeData.Builder unicodeDataBuilder;
  final String scxPropName;

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
        Aliases.normalize(propertyValue));
  }

  private void addPropertyValueAliases(String propertyName, String normalizedPropertyValue) {
    aliases.add(normalizedPropertyValue);
  }
}
