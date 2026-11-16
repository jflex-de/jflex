/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.generator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Stream;
import jflex.exceptions.GeneratorException;
import jflex.l10n.ErrorMessages;
import jflex.logging.Out;
import jflex.option.Options;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests that characters in the regular expressions of a spec must be in the character set that the
 * spec declares: 0-127 for {@code %7bit}, 0-255 for {@code %8bit}, and up to the maximum code point
 * of the Unicode version for {@code %unicode}, which is U+FFFF before Unicode 3.0.
 *
 * <p>A character in the spec outside of this range is an error.
 *
 * <p>Each case is a spec with the directives of the case and two rules: {@code x}, and the regular
 * expression of the case.
 */
public class SpecCharRangeTest {

  /** U+00E9, in {@code %8bit} and not in {@code %7bit}. */
  private static final String E_ACUTE = "\u00e9";

  /** U+00FF, the last character of {@code %8bit}. The upper case U+0178 is outside. */
  private static final String Y_DIAERESIS = "\u00ff";

  /** U+0101, not in {@code %8bit}. */
  private static final String A_MACRON = "\u0101";

  /** U+10400, above the maximum code point U+FFFF of Unicode 2.0. */
  private static final String DESERET = new String(Character.toChars(0x10400));

  private static final String DESERET_2 = new String(Character.toChars(0x10401));

  @TempDir File tmp;

  private static Arguments spec(String name, String directives, String regExp) {
    return Arguments.of(name, false, directives, regExp);
  }

  /** A spec for JLex mode ({@code --jlex}). */
  private static Arguments jlexSpec(String name, String directives, String regExp) {
    return Arguments.of(name, true, directives, regExp);
  }

  /** Specs that JFlex currently rejects. */
  static Stream<Arguments> rejected() {
    return Stream.of(
        spec("unicode2_escape", "%unicode 2.0", "\\u{10400}"),
        spec("unicode2_escape6", "%unicode 2.0", "\\U010400"),
        spec("unicode2_escapeInString", "%unicode 2.0", "\"a\\u{10400}\""),
        spec("unicode2_escapeInClass", "%unicode 2.0", "[\\u{10400}]"),
        spec("unicode2_caseless_escape", "%unicode 2.0\n%ignorecase", "\\u{10400}"));
  }

  /** Specs that JFlex should reject, but currently does not. */
  static Stream<Arguments> notYetRejected() {
    return Stream.of(
        // --- %7bit, as character input
        spec("7bit_char", "%7bit", E_ACUTE),
        spec("7bit_string", "%7bit", "\"" + E_ACUTE + "a\""),
        // makes the scanner match the empty string currently
        spec("7bit_stringEnd", "%7bit", "\"caf" + E_ACUTE + "\""),
        spec("7bit_class", "%7bit", "[a" + E_ACUTE + "]"),
        spec("7bit_classRange", "%7bit", "[a-" + E_ACUTE + "]"),
        spec("7bit_classNegated", "%7bit", "[^" + E_ACUTE + "]"),
        spec("7bit_classString", "%7bit", "[\"a" + E_ACUTE + "\"]"),
        spec("7bit_classOperator", "%7bit", "[a-z--[" + E_ACUTE + "]]"),
        spec("7bit_caseless_char", "%7bit\n%ignorecase", E_ACUTE),
        // --- %7bit, escape sequences
        spec("7bit_escapeUnicode4", "%7bit", "\\u00e9"),
        spec("7bit_escapeHex", "%7bit", "\\xE9"),
        spec("7bit_escapeOctal", "%7bit", "\\351"),
        spec("7bit_escapeCodePoint", "%7bit", "\\u{e9}"),
        spec("7bit_escapeFirstOutside", "%7bit", "\\x80"),
        spec("7bit_escapeSupplementary", "%7bit", "\\u{10400}"),
        spec("7bit_escapeInString", "%7bit", "\"caf\\u00e9\""),
        spec("7bit_escapeCodePointsInString", "%7bit", "\"a\\u{62 e9}\""),
        spec("7bit_escapeInClass", "%7bit", "[\\u00e9]"),
        spec("7bit_escapeInClassRange", "%7bit", "[\\x00-\\xFF]"),
        // --- %8bit
        spec("8bit_char", "%8bit", A_MACRON),
        spec("8bit_string", "%8bit", "\"" + A_MACRON + "\""),
        spec("8bit_class", "%8bit", "[" + A_MACRON + "]"),
        spec("8bit_escapeFirstOutside", "%8bit", "\\u0100"),
        spec("8bit_escapeInClassRange", "%8bit", "[\\x00-\\u0100]"),
        spec("full_char", "%full", A_MACRON),
        // --- a Unicode version with  maximum code point U+FFFF, as character input
        spec("unicode2_char", "%unicode 2.0", DESERET),
        spec("unicode2_string", "%unicode 2.0", "\"" + DESERET + "k\""),
        spec("unicode2_class", "%unicode 2.0", "[k" + DESERET + "]"),
        spec("unicode2_classRange", "%unicode 2.0", "[" + DESERET + "-" + DESERET_2 + "]"),
        spec("unicode1_1_char", "%unicode 1.1", DESERET),
        spec("unicode2_1_char", "%unicode 2.1", DESERET),
        // currently fails with ArrayIndexOutOfBoundsException
        spec("unicode2_caseless_char", "%unicode 2.0\n%ignorecase", DESERET),
        spec("unicode2_caseless_string", "%unicode 2.0\n%ignorecase", "\"" + DESERET + "k\""),
        jlexSpec("unicode2_caseless_jlex_char", "%unicode 2.0\n%ignorecase", DESERET),
        jlexSpec("unicode2_caseless_jlex_class", "%unicode 2.0\n%ignorecase", "[k" + DESERET + "]"),
        jlexSpec(
            "unicode2_caseless_jlex_classNegated",
            "%unicode 2.0\n%ignorecase",
            "[^" + DESERET + "]"),
        // --- character set is declared after the macro that uses a character outside the set
        spec("7bit_macroBeforeDirective", "M = " + E_ACUTE + "\n%7bit", "{M}"),
        spec("7bit_macroBeforeDirective_string", "M = \"caf" + E_ACUTE + "\"\n%7bit", "{M}"),
        spec("7bit_macroBeforeDirective_escape", "M = \\u00e9\n%7bit", "{M}"),
        spec("7bit_macroUnused", "%7bit\nM = " + E_ACUTE, "a"),
        // --- JLex mode
        jlexSpec("jlex_7bit_char", "%7bit", E_ACUTE),
        jlexSpec("jlex_7bit_class", "%7bit", "[" + E_ACUTE + "]"));
  }

  /** Specs with correct input/character set use. */
  static Stream<Arguments> accepted() {
    return Stream.of(
        // --- the last character of the character set
        spec("7bit_lastChar", "%7bit", "\\x7F"),
        spec("7bit_lastCharInClassRange", "%7bit", "[\\x00-\\x7F]"),
        spec("8bit_lastChar", "%8bit", Y_DIAERESIS),
        spec("8bit_lastCharEscape", "%8bit", "\\u00FF"),
        spec("8bit_lastCharInString", "%8bit", "\"caf" + E_ACUTE + Y_DIAERESIS + "\""),
        spec("8bit_lastCharInClassRange", "%8bit", "[\\x00-\\xFF]"),
        spec("unicode2_lastChar", "%unicode 2.0", "\\uFFFF"),
        spec("unicode2_lastCharInClassRange", "%unicode 2.0", "[\\u0000-\\uFFFF]"),
        spec("unicode3_supplementary", "%unicode 3.0", DESERET),
        spec("unicode3_supplementaryEscape", "%unicode 3.0", "\\u{10FFFF}"),
        spec("unicode_supplementary", "%unicode", "\"" + DESERET + "\""),
        spec("16bit_supplementary", "%16bit", DESERET),
        spec("default_supplementary", "", "[" + DESERET + "-" + DESERET_2 + "]"),
        // --- a case variant outside of the character set is not an error
        // U+212A KELVIN SIGN
        spec("7bit_caseless_variantOutside", "%7bit\n%ignorecase", "k"),
        spec("7bit_caseless_variantOutside_string", "%7bit\n%ignorecase", "\"ks\""),
        // U+0178, the upper case of U+00FF
        spec("8bit_caseless_variantOutside", "%8bit\n%ignorecase", Y_DIAERESIS),
        jlexSpec("jlex_7bit_caseless_variantOutside_class", "%7bit\n%ignorecase", "[a-z]"),
        // --- classes that are not written as characters may be larger than the character set
        spec("7bit_anyChar", "%7bit", "[^]"),
        spec("7bit_dot", "%7bit", "."),
        spec("7bit_negatedClass", "%7bit", "[^a]"),
        spec("7bit_negatedExpression", "%7bit", "!a"),
        spec("7bit_predefinedClass", "%7bit", "[:letter:]"),
        spec("7bit_predefinedClassNegated", "%7bit", "\\W"),
        spec("7bit_unicodeProperty", "%7bit", "\\p{L}"),
        spec("7bit_unicodePropertyNegated", "%7bit", "\\P{L}"),
        spec("8bit_predefinedClass", "%8bit", "[:jletterdigit:]"),
        spec("unicode2_anyChar", "%unicode 2.0", "[^]"),
        spec("unicode2_negatedClass", "%unicode 2.0", "[^a]"),
        spec("unicode2_caseless_negatedClass", "%unicode 2.0\n%ignorecase", "[^a]"),
        jlexSpec("unicode2_caseless_jlex_negatedClass", "%unicode 2.0\n%ignorecase", "[^a]"));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("rejected")
  public void outsideOfCharSet(String name, boolean jlex, String directives, String regExp)
      throws Exception {
    assertRejected(name, jlex, directives, regExp);
  }

  @Disabled(
      "JFlex bug: a character in a regular expression that is outside of the character set of the"
          + " spec is not reported as an error.")
  @ParameterizedTest(name = "{0}")
  @MethodSource("notYetRejected")
  public void outsideOfCharSet_notYetRejected(
      String name, boolean jlex, String directives, String regExp) throws Exception {
    assertRejected(name, jlex, directives, regExp);
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("accepted")
  public void insideOfCharSet(String name, boolean jlex, String directives, String regExp)
      throws Exception {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    String generated = generator(name, jlex, directives, regExp, output).generate();
    assertTrue(new File(generated).isFile(), describe(directives, regExp, output));
  }

  /** Checks that we report an error with the right error type. */
  private void assertRejected(String name, boolean jlex, String directives, String regExp)
      throws Exception {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    LexGenerator generator = generator(name, jlex, directives, regExp, output);
    GeneratorException e =
        assertThrows(
            GeneratorException.class,
            generator::generate,
            () -> "Must be rejected. " + describe(directives, regExp, output));
    assertFalse(
        e.isUnExpected(),
        () -> "JFlex failed with " + e.getCause() + ". " + describe(directives, regExp, output));
    String messages = output.toString(StandardCharsets.UTF_8.name());
    assertTrue(
        Stream.of(
                ErrorMessages.CS2SMALL_CHAR,
                ErrorMessages.CS2SMALL_STRING,
                ErrorMessages.CODEPOINT_OUT_OF_RANGE)
            .anyMatch(message -> messages.contains(ErrorMessages.get(message))),
        () ->
            "The error must be about a character outside of the character set. "
                + describe(directives, regExp, output));
  }

  /** Writes a spec for a test case and returns the generator for it. */
  private LexGenerator generator(
      String name, boolean jlex, String directives, String regExp, ByteArrayOutputStream output)
      throws Exception {
    // one directory per case: some names differ only in case
    File dir = new File(tmp, name);
    assertTrue(dir.mkdir());
    File spec = new File(dir, "spec.flex");
    Files.write(
        spec.toPath(),
        Arrays.asList(
            "%%",
            "%public",
            "%class Scanner",
            "%integer",
            directives,
            "%%",
            "x  { return 0; }",
            regExp + "  { return 1; }"),
        StandardCharsets.UTF_8);
    Options options = new Options();
    options.setDir(dir);
    options.setEncoding(StandardCharsets.UTF_8);
    options.setJLex(jlex);
    options.setVerbose(false);
    options.setProgress(false);
    return new LexGenerator(spec, options, new Out(options, output));
  }

  private static String describe(String directives, String regExp, ByteArrayOutputStream output) {
    return String.format(
        "Directives: %s, regular expression: %s, JFlex output:%n%s",
        directives.replace("\n", " "), regExp, output);
  }
}
