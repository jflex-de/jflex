package jflex.testing.testsuite.golden;

import static com.google.common.truth.Truth.assertWithMessage;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import jflex.testing.diff.DiffOutputStream;
import org.junit.After;

public class AbstractGoldenTest {

  private DiffOutputStream output;

  protected void compareSystemOutWith(GoldenInOutFilePair golden) throws FileNotFoundException {
    // in-memory output comparison
    output = new DiffOutputStream(Files.newReader(golden.outputFile, Charsets.UTF_8));
    System.setOut(new PrintStream(output));
  }

  @After
  public void checkOuputEntirelyGenerated() {
    assertWithMessage("All expected output has been printed on System.out")
        .that(output.isCompleted())
        .isTrue();
  }
}
