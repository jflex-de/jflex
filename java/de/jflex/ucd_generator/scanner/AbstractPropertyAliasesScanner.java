package de.jflex.ucd_generator.scanner;

import de.jflex.ucd_generator.ucd.UnicodeData;
import de.jflex.ucd_generator.util.PropertyNameNormalizer;
import java.util.HashSet;
import java.util.Set;

/** Scanner for {@code PropertyAliases(-X.X.X).txt}. */
abstract class AbstractPropertyAliasesScanner {

  final Set<String> aliases = new HashSet<>();
  final UnicodeData unicodeData;

  String longName;

  public AbstractPropertyAliasesScanner(UnicodeData unicodeData) {
    this.unicodeData = unicodeData;
  }

  void addPropertyAliases() {
    String normalizedLongName = PropertyNameNormalizer.normalize(longName);
    // Long names should resolve to themselves
    aliases.add(normalizedLongName);
    for (String alias : aliases) {
      unicodeData.addPropertyAlias(PropertyNameNormalizer.normalize(alias), normalizedLongName);
    }
    clear();
  }

  protected void clear() {
    longName = null;
    aliases.clear();
  }
}
