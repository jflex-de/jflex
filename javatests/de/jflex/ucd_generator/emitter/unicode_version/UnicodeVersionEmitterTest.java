package de.jflex.ucd_generator.emitter.unicode_version;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.ucd_generator.util.JavaStrings;
import org.junit.Test;

/** Unit test for {@link UnicodeVersionEmitter}. */
public class UnicodeVersionEmitterTest {
  @Test
  public void escapedUTF16Char() {
    assertThat(JavaStrings.escapedUTF16Char(0)).isEqualTo("\\000");
    assertThat(JavaStrings.escapedUTF16Char('\t')).isEqualTo("\\t");
    assertThat(JavaStrings.escapedUTF16Char('a')).isEqualTo("\\u0061");
    assertThat(JavaStrings.escapedUTF16Char(Character.toCodePoint('\ud800', '\udc0d')))
        .isEqualTo("\\ud800\\udc0d");
  }
}
