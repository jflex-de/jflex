package jflex.testing.testsuite.golden;

import static com.google.common.truth.Truth.assertWithMessage;

import com.google.common.base.Charsets;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import jflex.testing.diff.DiffOutputStream;
import org.junit.After;

public abstract class AbstractGoldenTest<T> {

  private DiffOutputStream output;

  protected void compareSystemOutWith(GoldenInOutFilePair golden) throws FileNotFoundException {
    // in-memory output comparison
    output = new DiffOutputStream(Files.newReader(golden.outputFile, Charsets.UTF_8));
    System.setOut(new PrintStream(output));
  }

  @After
  public void checkOuputEntirelyGenerated() {
    if (output == null) {
      // possible if there was no input, and we have a "scanner compiles" test.
      return;
    }
    assertWithMessage("All expected output has been printed on System.out")
        .that(output.remainingContent())
        .isEmpty();
  }

  protected T createScanner(File inputFile) throws FileNotFoundException {
    return createScanner(Files.newReader(inputFile, StandardCharsets.UTF_8));
  }

  protected T createScanner(String content) throws IOException {
    return createScanner(CharSource.wrap(content).openStream());
  }

  protected abstract T createScanner(Reader reader);
}
