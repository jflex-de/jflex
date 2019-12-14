package jflex.ucd_generator.scanner;

import java.util.HashSet;
import java.util.Set;

/** Scanner for {@code PropertyValueAliases(-X.X.X).txt}. */
public class AbstractPropertyValueAliasesScanner {

  final Set<String> aliases = new HashSet<>();

  final UnicodeData.Builder unicodeDataBuilder;
  final PropertyNameNormalizer propertyNameNormalizer;
  final String scxPropName;

  protected String propertyAlias;
  protected String propertyValue;

  public AbstractPropertyValueAliasesScanner(
      UnicodeData.Builder unicodeDataBuilder, PropertyNameNormalizer propertyNameNormalizer) {
    this.unicodeDataBuilder = unicodeDataBuilder;
    this.propertyNameNormalizer = propertyNameNormalizer;
    scxPropName = propertyNameNormalizer.getCanonicalPropertyName("Script_Extensions");
  }

  void addPropertyValueAliases() {
    //    ImmutableSet<String> immutableAliases = ImmutableSet.copyOf(aliases);
    //    unicodeDataBuilder.addPropertyValueAliases
    //        (propertyAlias, propertyValue, immutableAliases);
    //    String canonicalPropertyName
    //        = propertyNameNormalizer.getCanonicalPropertyName(propertyAlias);
    //    if ("script".equals(canonicalPropertyName)) {
    //      // Clone Script/sc property value aliases => Script_Extensions/scx
    //      unicodeDataBuilder.addPropertyValueAliases
    //          (scxPropName, propertyValue, immutableAliases);
    //    }
    //    aliases.clear();
  }
}
