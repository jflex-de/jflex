package jflex.testcase.charclass;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import com.google.common.io.CharSource;
import java.io.IOException;
import org.junit.After;
import org.junit.Test;

public class CharclassTest {

  private CharclassScanner scanner;

  @After
  public void endOfFile() throws Exception {
    assertThat(scanner.yylex()).isEqualTo(State.END_OF_FILE);
  }

  @Test
  public void nested() throws Exception {
    scanner =
        createScanner(
            "4 score and 7 years ago our fathers brought forth on this continent\n"
                + "a new nation, living and dead who struggled here have consecrated\n"
                + "it far above our poor power to add or detract.\n");
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // 4
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // score
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // and
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // 7
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // years
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // ago
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // our
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // fathers
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // brought
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // forth
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // on
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_GHIJKMTS); // this
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // continent
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_ABCDEF); // a
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // new
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // nation
    assertThat(scanner.yylex()).isEqualTo(State.INTER_PUNCTUATION1); // ,
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // living
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // and
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_ABCDEF); // dead
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // who
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // struggled
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // here
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // have
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // consecrated
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_GHIJKMTS); // it
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // far
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // above
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // our
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // poor
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // power
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // to
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_ABCDEF); // add
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // or
    assertThat(scanner.yylex()).isEqualTo(State.NESTED_AZ47); // detract
    assertWithMessage("/[-.~~-/]+/ matched '.'").that(scanner.yylex()).isEqualTo(State.SYM_2); // .
  }

  @Test
  public void test() throws Exception {
    scanner = createScanner("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    assertWithMessage("/[A-G--CG-Z]+/ matched 'AB'")
        .that(scanner.yylex())
        .isEqualTo(State.SUBSTRACT_AGCGZ);
    assertWithMessage("/[^[^ABC--ABZ]]/ matched 'C'")
        .that(scanner.yylex())
        .isEqualTo(State.NOT_ABC_ABZ);
    assertWithMessage("/[A-G--CG-Z]+/ matched 'DEF'")
        .that(scanner.yylex())
        .isEqualTo(State.SUBSTRACT_AGCGZ);
    assertWithMessage("/[^[^-GM---M]]+/ matched 'G'")
        .that(scanner.yylex())
        .isEqualTo(State.NOT_GMM);
    assertWithMessage("/[[H-L]--K]+/ matched 'HIJ'")
        .that(scanner.yylex())
        .isEqualTo(State.SUBSTRACT_HLK);
    assertWithMessage("/[^[^-KO--O]]+/ matched 'K'").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[[H-L]--K]+/ matched 'L'")
        .that(scanner.yylex())
        .isEqualTo(State.SUBSTRACT_HLK);
    assertWithMessage("/[-M-O--N]+/ matched 'M'")
        .that(scanner.yylex())
        .isEqualTo(State.SUBSTRACT_MON);
    assertWithMessage("/[-N-P---P]+/ matched 'NO'")
        .that(scanner.yylex())
        .isEqualTo(State.SUBSTRACT_NPP);
    assertWithMessage("/[QR[-]---R]+/ matched 'Q'")
        .that(scanner.yylex())
        .isEqualTo(State.SUBSTRACT_QRR);
    assertWithMessage("/[^[^R[-]---]]+/ matched 'R'").that(scanner.yylex()).isEqualTo(State.NOT_R);
    assertWithMessage("/[\\p{Lu}--A-RT-Z]+/ matched 'S'")
        .that(scanner.yylex())
        .isEqualTo(State.SUBSTRACT_PLU);
    assertWithMessage("/[\\p{L}&&[T||\\p{N}]]+/ matched 'T'")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_PLN);
    assertWithMessage("/[-TUV&&-UA]+/ matched 'U''")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_TUV_UA);
    assertWithMessage("/[-TUV&&VA]+/ matched 'V'")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_TUV_AV);
    assertWithMessage("/[VW&&-WA]+/ matched 'W'")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_VW_WA);
    assertWithMessage("/[XXX&&XYZ]+/ matched 'X'")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_XXX_XYZ);
    assertWithMessage("/[^[^-AYZ&&-YBM]]+/ matched 'Y'")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_AYZ_YMB);
    assertWithMessage("/[^[^-ZR&&ABZ]]+/ matched 'Z'")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_ZR_ABZ);
  }

  @Test
  public void subtraction() throws Exception {
    scanner =
        createScanner(
            ""
                + "-K-\n" // /[^[^-KO--O]]+/ matched `-K-`
                + "-G-\n"
                + "-MOON-\n"
                + "-NOON-\n"
                + "-QQQ-\n"
                + "-RRR-\n"
                + "SSS\n");
    assertWithMessage("/[^[^-KO--O]]+/ matched `-K-`")
        .that(scanner.yylex())
        .isEqualTo(State.NOT_KOO);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[^[^-GM---M]]+/ matched `G`")
        .that(scanner.yylex())
        .isEqualTo(State.NOT_GMM);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[-M-O--N]+/ matched `-MOO`")
        .that(scanner.yylex())
        .isEqualTo(State.SUBSTRACT_MON);
    assertWithMessage("/[-N-P---P]+/ matched `N`")
        .that(scanner.yylex())
        .isEqualTo(State.SUBSTRACT_NPP);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[-N-P---P]+/ matched `NOON`")
        .that(scanner.yylex())
        .isEqualTo(State.SUBSTRACT_NPP);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[QR[-]---R]+/ matched `QQQ`")
        .that(scanner.yylex())
        .isEqualTo(State.SUBSTRACT_QRR);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[^[^R[-]---]]+/ matched `RRR`")
        .that(scanner.yylex())
        .isEqualTo(State.NOT_R);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[\\p{Lu}--A-RT-Z]+/ matched `SSS`")
        .that(scanner.yylex())
        .isEqualTo(State.SUBSTRACT_PLU);
  }

  @Test
  public void inter() throws Exception {
    scanner =
        createScanner(
            ""
                + "TTT\n" // /[\p{L}&&[T||\p{N}]]+/ matched `TTT`
                + "-UUU-\n"
                + "-VVV-\n"
                + "-WWW-\n"
                + "-XXX-\n"
                + "-YYY-\n"
                + "-ZZZ-");
    assertWithMessage("/[\\p{L}&&[T||\\p{N}]]+/ matched `TTT`")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_PLN);
    assertWithMessage("/[-TUV&&-UA]+/ matched `-UUU-`")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_TUV_UA);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[-TUV&&VA]+/ matched `VVV`")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_TUV_AV);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[VW&&-WA]+/ matched `WWW`")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_VW_WA);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[XXX&&XYZ]+/ matched `XXX`")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_XXX_XYZ);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[^[^-AYZ&&-YBM]]+/ matched `-YYY-`")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_AYZ_YMB);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[^[^-ZR&&ABZ]]+/ matched `ZZZ`")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_ZR_ABZ);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
  }

  @Test
  public void symetricDifference() throws Exception {
    scanner =
        createScanner(
            "-,,,-\n"
                + "-!!!-\n"
                + "&+&+&\n"
                + "-//..-\n"
                + "(-(((-)\n"
                + "[-[[[-]\n"
                + "!$```$!\n"
                + "-_!_||_!_-\n"
                + "<-@@@->\n"
                + "{-\\\\\\-}\n"
                + "/??\";\"??/\n");
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[^[^,.:&&-*(),]]+/ matched `,,,`")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_PUNCTUATION1);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[^[^!&#&&\\^~!]]+/ matched `!!!`")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_PUNCTUATION2);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[[\\p{L}||+]~~[\\p{L}||&]]+/ matched `&+&+&`")
        .that(scanner.yylex())
        .isEqualTo(State.SYM_1);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[-.~~-/]+/ matched `//..`").that(scanner.yylex()).isEqualTo(State.SYM_2);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[-()~~)]+/ matched `(-(((-`").that(scanner.yylex()).isEqualTo(State.SYM_3);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[\\[\\]~~\\[-]+/ matched `-]`")
        .that(scanner.yylex())
        .isEqualTo(State.SYM_4);
    assertWithMessage("/[^[^!&#&&\\^~!]]+/ matched `!`")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_PUNCTUATION2);
    assertWithMessage("/[#!~~!#`$]+/ matched `$```$`").that(scanner.yylex()).isEqualTo(State.SYM_5);
    assertWithMessage("/[^[^!&#&&\\^~!]]+/ matched `!`")
        .that(scanner.yylex())
        .isEqualTo(State.INTER_PUNCTUATION2);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[^[^-=%_|~~-%=!]]+/ matched `_!_||_!_`")
        .that(scanner.yylex())
        .isEqualTo(State.SYM_6);
    assertWithMessage("/[^[^-KO--O]]+/ matched `-`").that(scanner.yylex()).isEqualTo(State.NOT_KOO);
    assertWithMessage("/[^[^-<>~~<@>]]+/ matched `-@@@-`")
        .that(scanner.yylex())
        .isEqualTo(State.SYM_7);
    assertWithMessage("/[^[^{}~~-{\\\\}]]+/ matched `-\\\\\\-`")
        .that(scanner.yylex())
        .isEqualTo(State.SYM_8);
    assertWithMessage("/[-.~~-/]+/ matched `/`").that(scanner.yylex()).isEqualTo(State.SYM_2);
    assertWithMessage("/[^[^;?/~~\"?/]]+/ matched `\";\"`")
        .that(scanner.yylex())
        .isEqualTo(State.SYM_9);
    assertWithMessage("/[-.~~-/]+/ matched `/`").that(scanner.yylex()).isEqualTo(State.SYM_2);
  }

  private static CharclassScanner createScanner(String content) throws IOException {
    return new CharclassScanner(CharSource.wrap(content).openStream());
  }
}
