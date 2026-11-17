/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Stream;
import jflex.core.unicode.CharClasses;
import jflex.core.unicode.IntCharSet;
import jflex.logging.Out;
import jflex.option.Options;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the encoding option ({@code --encoding}). Check that JFlex reads the spec in this encoding,
 * shows lines of the spec in error messages in that encoding, and writes the generated scanner in
 * the encoding.
 *
 * <p>Each spec has a character that differs between the test encodings.
 */
public class SpecEncodingTest {

  /** U+00E4. One byte in ISO-8859-1, two bytes in UTF-8 and in UTF-16. */
  private static final String A_UMLAUT = "ä";

  private static final String UMLAUT_RULE = "\"ä\"  { return 1; }";

  @TempDir File tmp;

  static Stream<Charset> encodings() {
    return Stream.of(StandardCharsets.ISO_8859_1, StandardCharsets.UTF_16, StandardCharsets.UTF_8);
  }

  /** Writes the lines to a file in the directory of the test, in the given encoding. */
  private File write(String name, Charset encoding, String... lines) throws Exception {
    File file = new File(tmp, name);
    Files.write(file.toPath(), Arrays.asList(lines), encoding);
    return file;
  }

  /** Writes a spec with the given rules, in the given encoding. */
  private File spec(Charset encoding, String... rules) throws Exception {
    String[] lines = {"%%", "%public", "%class Scanner", "%integer", "%unicode", "%%"};
    lines = Arrays.copyOf(lines, lines.length + rules.length);
    System.arraycopy(rules, 0, lines, lines.length - rules.length, rules.length);
    return write("spec.flex", encoding, lines);
  }

  /** Returns a generator that reads the spec in the given encoding. */
  private LexGenerator generator(File spec, Charset encoding, ByteArrayOutputStream output) {
    Options options = new Options();
    options.setDir(tmp);
    options.setEncoding(encoding);
    options.setVerbose(false);
    options.setProgress(false);
    return new LexGenerator(spec, options, new Out(options, output));
  }

  /** Asserts that the character is the only one in its character class. */
  private static void assertOwnClass(int codePoint, LexGenerator generator, Object output) {
    CharClasses classes = generator.charClasses();
    assertEquals(
        IntCharSet.ofCharacter(codePoint),
        classes.getCharClass(classes.getClassCode(codePoint)),
        () ->
            String.format(
                "Class of U+%04X. Character classes: %s, JFlex output: %s",
                codePoint, classes, output));
  }

  @ParameterizedTest
  @MethodSource("encodings")
  public void specIsReadInEncoding(Charset encoding) throws Exception {
    File spec = spec(encoding, "x  { return 0; }", UMLAUT_RULE);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    LexGenerator generator = generator(spec, encoding, output);
    generator.generate();
    assertOwnClass(A_UMLAUT.codePointAt(0), generator, output);
    assertOwnClass('x', generator, output);
    // x, the umlaut, and all other characters
    assertEquals(3, generator.charClassesCount());
  }

  @Test
  public void specInOtherEncodingHasOtherCharacters() throws Exception {
    File spec = spec(StandardCharsets.UTF_8, "x  { return 0; }", UMLAUT_RULE);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    LexGenerator generator = generator(spec, StandardCharsets.ISO_8859_1, output);
    generator.generate();
    assertOwnClass(0xC3, generator, output);
    assertOwnClass(0xA4, generator, output);
    assertOwnClass('x', generator, output);
    assertEquals(4, generator.charClassesCount());
  }

  @ParameterizedTest
  @MethodSource("encodings")
  public void includedFileIsReadInEncoding(Charset encoding) throws Exception {
    write("umlaut.inc", encoding, UMLAUT_RULE);
    File spec = spec(encoding, "x  { return 0; }", "%include umlaut.inc");
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    LexGenerator generator = generator(spec, encoding, output);
    generator.generate();
    assertOwnClass(A_UMLAUT.codePointAt(0), generator, output);
    assertEquals(3, generator.charClassesCount());
  }

  /** The code of an action is in the generated scanner as it is in the spec. */
  @ParameterizedTest
  @MethodSource("encodings")
  public void scannerIsWrittenInEncoding(Charset encoding) throws Exception {
    String code = "return \"ä\".length();";
    File spec = spec(encoding, "x  { " + code + " }");
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    String generated = generator(spec, encoding, output).generate();
    String scanner = new String(Files.readAllBytes(new File(generated).toPath()), encoding);
    assertTrue(scanner.contains(code), () -> "Action in " + generated + ": " + scanner);
    assertTrue(scanner.contains("class Scanner"), () -> "Class in " + generated + ": " + scanner);
  }

  /** Warning shows the line of the spec with correct content. */
  @ParameterizedTest
  @MethodSource("encodings")
  public void messageShowsSpecLineInEncoding(Charset encoding) throws Exception {
    String neverMatched = "\"ä\"  { return 2; }";
    File spec = spec(encoding, UMLAUT_RULE, neverMatched);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    generator(spec, encoding, output).generate();
    // JFlex writes its messages in the default encoding of the platform
    Charset platform = Charset.defaultCharset();
    String messages = new String(output.toByteArray(), platform);
    String line = new String(neverMatched.getBytes(platform), platform);
    assertTrue(messages.contains(line), () -> "Expected the line " + line + " in: " + messages);
  }
}
