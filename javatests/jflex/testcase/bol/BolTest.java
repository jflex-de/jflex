package jflex.testcase.bol;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Test;

/**
 * Tests BOL and EOL operators.
 */
public class BolTest {
    @Test
    public void test0() throws Exception{
        BolScanner scanner = createScanner("bol-0.input");
        assertThat(scanner.state).isEqualTo(State.INITIAL);
        assertThat(scanner.yylex()).isEqualTo(State.HELLO_AT_BOL_AND_EOL);
        assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
        assertThat(scanner.yylex()).isEqualTo(State.SPACE);
        assertThat(scanner.yylex()).isEqualTo(State.SPACE);
        assertThat(scanner.yylex()).isEqualTo(State.HELLO_AT_EOL);
        assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
        assertThat(scanner.yylex()).isEqualTo(State.SPACE);
        assertThat(scanner.yylex()).isEqualTo(State.SPACE);
        assertThat(scanner.yylex()).isEqualTo(State.HELLO_SIMPLY);
        assertThat(scanner.yylex()).isEqualTo(State.SPACE);
        assertThat(scanner.yylex()).isEqualTo(State.SPACE);
        assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
        assertThat(scanner.yylex()).isEqualTo(State.HELLO_AT_BOL);
        assertThat(scanner.yylex()).isEqualTo(State.SPACE);
        assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
        assertThat(scanner.yylex()).isEqualTo(State.SPACE);
        assertThat(scanner.yylex()).isEqualTo(State.SPACE);
        assertThat(scanner.yylex()).isEqualTo(State.HELLO_AT_EOL);
        assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
        assertThat(scanner.yylex()).isEqualTo(State.HELLO_AT_BOL);
        assertThat(scanner.yylex()).isEqualTo(State.SPACE);
        assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
        assertThat(scanner.yylex()).isEqualTo(State.SPACE);
        assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
        assertThat(scanner.yylex()).isEqualTo(State.OTHER);
        assertThat(scanner.yylex()).isEqualTo(State.OTHER);
        assertThat(scanner.yylex()).isEqualTo(State.OTHER);
        assertThat(scanner.yylex()).isEqualTo(State.OTHER);
        assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
        assertThat(scanner.yylex()).isEqualTo(State.END_OF_FILE);
        assertThat(scanner.yylex()).isEqualTo(State.END_OF_FILE);
        assertThat(scanner.yylex()).isEqualTo(State.END_OF_FILE);
    }

    private static BolScanner createScanner(final String inputFileName) throws FileNotFoundException {
        File inputFile = new File(
            "javatests/jflex/testcase/bol/" +
            inputFileName);
        if (!inputFile.exists()) {
            throw new FileNotFoundException(inputFile.getAbsolutePath());
        }
            return new BolScanner(Files.newReader(inputFile,
                Charsets.UTF_8));
    }
}
