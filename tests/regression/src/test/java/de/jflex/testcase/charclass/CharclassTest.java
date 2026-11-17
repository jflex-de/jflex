/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.charclass;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** Test character class syntax. */
public class CharclassTest {

  private CharclassScanner scanner;

  @AfterEach
  public void endOfFile() throws Exception {
    assertEquals(State.END_OF_FILE, scanner.yylex());
  }

  @Test
  public void nested() throws Exception {
    scanner =
        createScanner(
            "4 score and 7 years ago our fathers brought forth on this continent\n"
                + "a new nation, living and dead who struggled here have consecrated\n"
                + "it far above our poor power to add or detract.\n");
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // 4
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // score
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // and
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // 7
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // years
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // ago
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // our
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // fathers
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // brought
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // forth
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // on
    assertEquals(State.NESTED_GHIJKMTS, scanner.yylex()); // this
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // continent
    assertEquals(State.NESTED_ABCDEF, scanner.yylex()); // a
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // new
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // nation
    assertEquals(State.INTER_PUNCTUATION1, scanner.yylex()); // ,
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // living
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // and
    assertEquals(State.NESTED_ABCDEF, scanner.yylex()); // dead
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // who
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // struggled
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // here
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // have
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // consecrated
    assertEquals(State.NESTED_GHIJKMTS, scanner.yylex()); // it
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // far
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // above
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // our
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // poor
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // power
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // to
    assertEquals(State.NESTED_ABCDEF, scanner.yylex()); // add
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // or
    assertEquals(State.NESTED_AZ47, scanner.yylex()); // detract
    assertEquals(State.SYM_2, scanner.yylex(), "/[-.~~-/]+/ matched '.'"); // .
  }

  @Test
  public void test() throws Exception {
    scanner = createScanner("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    assertEquals(State.SUBSTRACT_AGCGZ, scanner.yylex(), "/[A-G--CG-Z]+/ matched 'AB'");
    assertEquals(State.NOT_ABC_ABZ, scanner.yylex(), "/[^[^ABC--ABZ]]/ matched 'C'");
    assertEquals(State.SUBSTRACT_AGCGZ, scanner.yylex(), "/[A-G--CG-Z]+/ matched 'DEF'");
    assertEquals(State.NOT_GMM, scanner.yylex(), "/[^[^-GM---M]]+/ matched 'G'");
    assertEquals(State.SUBSTRACT_HLK, scanner.yylex(), "/[[H-L]--K]+/ matched 'HIJ'");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched 'K'");
    assertEquals(State.SUBSTRACT_HLK, scanner.yylex(), "/[[H-L]--K]+/ matched 'L'");
    assertEquals(State.SUBSTRACT_MON, scanner.yylex(), "/[-M-O--N]+/ matched 'M'");
    assertEquals(State.SUBSTRACT_NPP, scanner.yylex(), "/[-N-P---P]+/ matched 'NO'");
    assertEquals(State.SUBSTRACT_QRR, scanner.yylex(), "/[QR[-]---R]+/ matched 'Q'");
    assertEquals(State.NOT_R, scanner.yylex(), "/[^[^R[-]---]]+/ matched 'R'");
    assertEquals(State.SUBSTRACT_PLU, scanner.yylex(), "/[\\p{Lu}--A-RT-Z]+/ matched 'S'");
    assertEquals(State.INTER_PLN, scanner.yylex(), "/[\\p{L}&&[T||\\p{N}]]+/ matched 'T'");
    assertEquals(State.INTER_TUV_UA, scanner.yylex(), "/[-TUV&&-UA]+/ matched 'U''");
    assertEquals(State.INTER_TUV_AV, scanner.yylex(), "/[-TUV&&VA]+/ matched 'V'");
    assertEquals(State.INTER_VW_WA, scanner.yylex(), "/[VW&&-WA]+/ matched 'W'");
    assertEquals(State.INTER_XXX_XYZ, scanner.yylex(), "/[XXX&&XYZ]+/ matched 'X'");
    assertEquals(State.INTER_AYZ_YMB, scanner.yylex(), "/[^[^-AYZ&&-YBM]]+/ matched 'Y'");
    assertEquals(State.INTER_ZR_ABZ, scanner.yylex(), "/[^[^-ZR&&ABZ]]+/ matched 'Z'");
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
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-K-`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.NOT_GMM, scanner.yylex(), "/[^[^-GM---M]]+/ matched `G`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.SUBSTRACT_MON, scanner.yylex(), "/[-M-O--N]+/ matched `-MOO`");
    assertEquals(State.SUBSTRACT_NPP, scanner.yylex(), "/[-N-P---P]+/ matched `N`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.SUBSTRACT_NPP, scanner.yylex(), "/[-N-P---P]+/ matched `NOON`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.SUBSTRACT_QRR, scanner.yylex(), "/[QR[-]---R]+/ matched `QQQ`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.NOT_R, scanner.yylex(), "/[^[^R[-]---]]+/ matched `RRR`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.SUBSTRACT_PLU, scanner.yylex(), "/[\\p{Lu}--A-RT-Z]+/ matched `SSS`");
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
    assertEquals(State.INTER_PLN, scanner.yylex(), "/[\\p{L}&&[T||\\p{N}]]+/ matched `TTT`");
    assertEquals(State.INTER_TUV_UA, scanner.yylex(), "/[-TUV&&-UA]+/ matched `-UUU-`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.INTER_TUV_AV, scanner.yylex(), "/[-TUV&&VA]+/ matched `VVV`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.INTER_VW_WA, scanner.yylex(), "/[VW&&-WA]+/ matched `WWW`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.INTER_XXX_XYZ, scanner.yylex(), "/[XXX&&XYZ]+/ matched `XXX`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.INTER_AYZ_YMB, scanner.yylex(), "/[^[^-AYZ&&-YBM]]+/ matched `-YYY-`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.INTER_ZR_ABZ, scanner.yylex(), "/[^[^-ZR&&ABZ]]+/ matched `ZZZ`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
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
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.INTER_PUNCTUATION1, scanner.yylex(), "/[^[^,.:&&-*(),]]+/ matched `,,,`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.INTER_PUNCTUATION2, scanner.yylex(), "/[^[^!&#&&\\^~!]]+/ matched `!!!`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.SYM_1, scanner.yylex(), "/[[\\p{L}||+]~~[\\p{L}||&]]+/ matched `&+&+&`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.SYM_2, scanner.yylex(), "/[-.~~-/]+/ matched `//..`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.SYM_3, scanner.yylex(), "/[-()~~)]+/ matched `(-(((-`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.SYM_4, scanner.yylex(), "/[\\[\\]~~\\[-]+/ matched `-]`");
    assertEquals(State.INTER_PUNCTUATION2, scanner.yylex(), "/[^[^!&#&&\\^~!]]+/ matched `!`");
    assertEquals(State.SYM_5, scanner.yylex(), "/[#!~~!#`$]+/ matched `$```$`");
    assertEquals(State.INTER_PUNCTUATION2, scanner.yylex(), "/[^[^!&#&&\\^~!]]+/ matched `!`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.SYM_6, scanner.yylex(), "/[^[^-=%_|~~-%=!]]+/ matched `_!_||_!_`");
    assertEquals(State.NOT_KOO, scanner.yylex(), "/[^[^-KO--O]]+/ matched `-`");
    assertEquals(State.SYM_7, scanner.yylex(), "/[^[^-<>~~<@>]]+/ matched `-@@@-`");
    assertEquals(State.SYM_8, scanner.yylex(), "/[^[^{}~~-{\\\\}]]+/ matched `-\\\\\\-`");
    assertEquals(State.SYM_2, scanner.yylex(), "/[-.~~-/]+/ matched `/`");
    assertEquals(State.SYM_9, scanner.yylex(), "/[^[^;?/~~\"?/]]+/ matched `\";\"`");
    assertEquals(State.SYM_2, scanner.yylex(), "/[-.~~-/]+/ matched `/`");
  }

  private static CharclassScanner createScanner(String content) throws IOException {
    return new CharclassScanner(new StringReader(content));
  }
}
