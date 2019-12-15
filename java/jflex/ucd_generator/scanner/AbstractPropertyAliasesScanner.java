package jflex.ucd_generator.scanner;

import java.util.HashSet;
import java.util.Set;
import jflex.ucd_generator.scanner.model.UnicodeData;
import jflex.ucd_generator.util.PropertyNameNormalizer;

/** Scanner for {@code PropertyAliases(-X.X.X).txt}. */
public abstract class AbstractPropertyAliasesScanner {

  final Set<String> aliases = new HashSet<>();
  final UnicodeData.Builder unicodeDataBuilder;

  String longName;

  public AbstractPropertyAliasesScanner(UnicodeData.Builder unicodeDataBuilder) {
    this.unicodeDataBuilder = unicodeDataBuilder;
  }

  void addPropertyAliases() {
    String normalizedLongName = PropertyNameNormalizer.normalize(longName);
    // Long names should resolve to themselves
    aliases.add(normalizedLongName);
    for (String alias : aliases) {
      unicodeDataBuilder.addPropertyAlias(alias, normalizedLongName);
    }
    clear();
  }

  protected void clear() {
    longName = null;
    aliases.clear();
  }
}
