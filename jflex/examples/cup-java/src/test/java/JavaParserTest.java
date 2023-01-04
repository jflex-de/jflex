/*
 * Copyright 2020, Gerwin Klein, Régis Décamps, Steve Rowe
 * SPDX-License-Identifier: GPL-2.0-only
 */

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import com.google.common.base.Objects;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This is an integration test for the java parser example.
 *
 * @author Régis Décamps
 * @author Gerwin Klein
 */
public class JavaParserTest {

  private ByteArrayOutputStream outputStream;

  @Before
  public void setUp() {
    // the lexer driver prints status on stdout. Redirect to ByteOutputStream.
    outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
  }

  @After
  public void tearDown() throws Exception {
    outputStream.close();
  }

  /** Tests that the java parser does not System.exit(1). */
  @Test
  public void parseTest() throws Exception {
    File inputFile = openFile("src/main/java/TestLexer.java");
    assertThat(inputFile.isFile()).isTrue();

    String[] argv = new String[] {inputFile.getPath()};

    JavaParser.main(argv); // will System.exit(1) on error
  }

  /** Tests that the java lexer behaves as expected. */
  @Test
  public void lexTest() throws Exception {
    File inputFile = openFile("src/main/java/TestLexer.java");
    assertThat(inputFile.isFile()).isTrue();

    String[] argv = new String[] {inputFile.getPath()};

    TestLexer.main(argv);

    // test actual is expected
    File expected = openFile("src/test/data/lexer-output.good");
    assertThat(expected.isFile()).isTrue();

    BufferedReader actualContent = readOutputStream();
    BufferedReader expectedContent = new BufferedReader(new FileReader(expected));

    for (int lineNumber = 1; lineNumber != -1; lineNumber++) {
      String expectedLine = expectedContent.readLine();
      String actualLine = actualContent.readLine();
      assertWithMessage("Line " + lineNumber).that(actualLine).isEqualTo(expectedLine);
      if (expectedLine == null) lineNumber = -2; // EOF
    }
  }

  private BufferedReader readOutputStream() {
    byte[] rawOutput = outputStream.toByteArray();
    return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(rawOutput)));
  }

  /**
   * Opens the given file.
   *
   * <p>This method also works around a build difficulty:
   *
   * <ul>
   *   <li>Maven uses the directory that contains {@code pom.xml} as a working directory, i.e.
   *       {@code examples/simple}
   *   <li>ant uses the directory that contains {@code build.xml} as a working directory, i.e.
   *       {@code examples/simple}
   *   <li>bazel uses the directory that contains {@code WORKSPACE} as a working directory, i.e.
   *       {@code __main__} in <em>runfiles</em>.
   * </ul>
   */
  private File openFile(String pathName) throws IOException {
    String path = pathName;
    File pwd = new File(".").getCanonicalFile();
    assertThat(pwd.isDirectory()).isTrue();
    if (Objects.equal(pwd.getName(), "__main__")) {
      path = "jflex/examples/cup-java/" + path;
    }
    File file = new File(path);
    if (!file.isFile()) {
      throw new FileNotFoundException(path);
    }
    return file;
  }
}
