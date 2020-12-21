package de.jflex.ucd_generator.emitter.unicode_version;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/** Unit test for {@link UnicodeVersionEmitter}. */
public class UnicodeVersionEmitterTest {
  @Test
  public void escapedUTF16Char() {
    assertThat(UnicodeVersionEmitter.escapedUTF16Char(0)).isEqualTo("\\000");
    assertThat(UnicodeVersionEmitter.escapedUTF16Char('\t')).isEqualTo("\\t");
    assertThat(UnicodeVersionEmitter.escapedUTF16Char('a')).isEqualTo("\\u0061");
    assertThat(UnicodeVersionEmitter.escapedUTF16Char(Character.toCodePoint('\ud800', '\udc0d')))
        .isEqualTo("\\ud800\\udc0d");
  }
}
