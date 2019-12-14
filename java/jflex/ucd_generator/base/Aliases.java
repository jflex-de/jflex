package jflex.ucd_generator.base;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import java.util.Locale;
import java.util.regex.Pattern;

@AutoValue
public abstract class Aliases {

  /** Pattern used to normalize property value identifiers */
  private static final Pattern WORD_SEP_PATTERN = Pattern.compile("[-_\\s()]");

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

  abstract String canonicalName();

  abstract ImmutableSet<String> aliases();

  static Builder builder() {
    return new AutoValue_Aliases.Builder();
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder canonicalName(String canonicalName);
    //    abstract Builder aliases(ImmutableSet<String> aliases);
    abstract ImmutableSet.Builder<String> aliasesBuilder();

    abstract Aliases build();

    Builder name(String name) {
      return canonicalName(normalize(name));
    }

    Builder addAlias(String alias) {
      aliasesBuilder().add(alias);
      return this;
    }
  }
}
