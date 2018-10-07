import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import junit.framework.TestCase;

/**
 * This is an integration test.
 *
 * <p>The class Yylex is generated by JFLex from {@code src/main/jflex/simple.flex}.
 *
 * @author Régis Décamps
 */
public class YylexTest extends TestCase {

  private ByteArrayOutputStream outputStream;

  @Override
  public void setUp() {
    // the Yylex prints status on stdout. Redirect to ByteOutputStream.
    outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
  }

  @Override
  public void tearDown() throws Exception {
    outputStream.close();
  }

  /** Tests that the generated {@link Yylex} lexer behaves like expected. */
  public void testOutput() throws Exception {
    File inputFile = openFile("src/test/data/test.txt");
    assertThat(inputFile.isFile()).isTrue();

    String[] argv = new String[] {inputFile.getPath()};

    Yylex.main(argv);

    // test actual is expected
    File expected = new File("src/test/resources/output.good");
    assertWithMessage(expected.getAbsolutePath() + " should be a file")
        .that(expected.isFile())
        .isTrue();

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

  private File openFile(String pathName) throws FileNotFoundException {
    File file = new File(pathName);
    if (!file.isFile()) {
      throw new FileNotFoundException(pathName);
    }
    return file;
  }
}
