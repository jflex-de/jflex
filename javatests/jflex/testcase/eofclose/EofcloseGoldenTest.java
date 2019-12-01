// test: eofclose

package jflex.testcase.eofclose;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import jflex.testing.testsuite.golden.GoldenInOutFilePair;
import jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * test eofclose directive
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class EofcloseGoldenTest extends AbstractGoldenTest {

  /** Creates a scanner conforming to the {@code eofclose.flex} specification. */
  private final ScannerFactory<Eofclose> scannerFactory = ScannerFactory.of(Eofclose::new);

  private File testRuntimeDir = new File("javatests/jflex/testcase/eofclose");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "eofclose-0.input"),
            new File(testRuntimeDir, "eofclose-0.output"));
    compareSystemOutWith(golden);

    Eofclose scanner = scannerFactory.createScannerForFile(golden.inputFile);
    scanner.yylex();
  }

  public static void main(File inputFile) throws IOException {
    Reader reader = Files.newReader(inputFile, StandardCharsets.UTF_8);
    Eofclose scanner = new Eofclose(reader);
    scanner.yylex();
    reader.read();
    System.out.println("Reader still open.");
  }
}
