// test: count

package jflex.testcase.count;

import static com.google.common.truth.Truth.assertWithMessage;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.List;
import javax.annotation.Generated;
import jflex.testing.diff.DiffOutputStream;
import jflex.testing.testsuite.golden.GoldenInOutFilePair;
import org.junit.Test;

/**
 * test line/column/char counting
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
@Generated("jflex.migration.Migrator")
public class CountGoldenTest {

  @Test
  public void goldenTest() throws Exception {

    // The .input / .output Golden files
    File testRuntimeDir = new File("javatests/jflex/testcase/count");
    List<GoldenInOutFilePair> goldenFiles =
        ImmutableList.of(
            new GoldenInOutFilePair(
                new File(testRuntimeDir, "count-0.input"),
                new File(testRuntimeDir, "count-0.output")));

    for (GoldenInOutFilePair golden : goldenFiles) {
      // in-memory output comparison
      DiffOutputStream output =
          new DiffOutputStream(Files.newReader(golden.outputFile, Charsets.UTF_8));
      System.setOut(new PrintStream(output));

      // Scanner for /Users/regis/Projects/jflex/testsuite/testcases/src/test/cases/count/count.flex
      Count scanner = createScanner(golden.inputFile);
      scanner.yylex();

      assertWithMessage("All expected output has been printed on System.out")
          .that(output.remainingContent())
          .isEmpty();
    }
  }

  private static Count createScanner(File inputFile) throws FileNotFoundException {
    return new Count(Files.newReader(inputFile, Charset.forName("UTF-8")));
  }
}
