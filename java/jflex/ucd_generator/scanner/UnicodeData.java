package jflex.ucd_generator.scanner;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UnicodeData {

    @AutoValue.Builder
    abstract static class Builder {
        UnicodeData build() {
            return new AutoValue_UnicodeData();
        }

        abstract Builder addCaselessMatches();

        abstract Builder addInterval();
    }
}
