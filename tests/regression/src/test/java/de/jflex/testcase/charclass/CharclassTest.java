/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.charclass;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/** Test character class syntax. */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/charclass/charclass.flex",
    // no warnings
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 35,
    charClassesCount = 34,
    sameCharClass = {
      "47nopqruvwxyz",
      "abcdef",
      "ghijklmst",
      "ABDEF",
      "HIJL",
      // [\p{Lu}--A-RT-Z]
      "S\u00C0\u0391",
      "\";",
      "$`",
      "&+",
      "./",
      "_|",
      " \n#%\'P[^{}0\u00E4"
    },
    distinctCharClasses = {"!\"$&(,-.4@ACGHKMNOQRSTUVWXYZ\\]_ag "})
public class CharclassTest implements JFlexTest {

  private CharclassScanner scanner;

  /** Checks rule and text of the next token. */
  private void assertToken(State rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext());
  }

  private void assertEof() throws IOException {
    assertEquals(State.END_OF_FILE, scanner.yylex());
  }

  @Test
  public void nested() throws Exception {
    scanner =
        createScanner(
            "4 score and 7 years ago our fathers brought forth on this continent\n"
                + "a new nation, living and dead who struggled here have consecrated\n"
                + "it far above our poor power to add or detract.\n");
    assertToken(State.NESTED_AZ47, "4");
    assertToken(State.NESTED_AZ47, "score");
    assertToken(State.NESTED_AZ47, "and");
    assertToken(State.NESTED_AZ47, "7");
    assertToken(State.NESTED_AZ47, "years");
    assertToken(State.NESTED_AZ47, "ago");
    assertToken(State.NESTED_AZ47, "our");
    assertToken(State.NESTED_AZ47, "fathers");
    assertToken(State.NESTED_AZ47, "brought");
    assertToken(State.NESTED_AZ47, "forth");
    assertToken(State.NESTED_AZ47, "on");
    assertToken(State.NESTED_GHIJKMTS, "this");
    assertToken(State.NESTED_AZ47, "continent");
    assertToken(State.NESTED_ABCDEF, "a");
    assertToken(State.NESTED_AZ47, "new");
    assertToken(State.NESTED_AZ47, "nation");
    assertToken(State.INTER_PUNCTUATION1, ",");
    assertToken(State.NESTED_AZ47, "living");
    assertToken(State.NESTED_AZ47, "and");
    assertToken(State.NESTED_ABCDEF, "dead");
    assertToken(State.NESTED_AZ47, "who");
    assertToken(State.NESTED_AZ47, "struggled");
    assertToken(State.NESTED_AZ47, "here");
    assertToken(State.NESTED_AZ47, "have");
    assertToken(State.NESTED_AZ47, "consecrated");
    assertToken(State.NESTED_GHIJKMTS, "it");
    assertToken(State.NESTED_AZ47, "far");
    assertToken(State.NESTED_AZ47, "above");
    assertToken(State.NESTED_AZ47, "our");
    assertToken(State.NESTED_AZ47, "poor");
    assertToken(State.NESTED_AZ47, "power");
    assertToken(State.NESTED_AZ47, "to");
    assertToken(State.NESTED_ABCDEF, "add");
    assertToken(State.NESTED_AZ47, "or");
    assertToken(State.NESTED_AZ47, "detract");
    assertToken(State.SYM_2, ".");
    assertEof();
  }

  @Test
  public void test() throws Exception {
    scanner = createScanner("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    assertToken(State.SUBSTRACT_AGCGZ, "AB");
    assertToken(State.NOT_ABC_ABZ, "C");
    assertToken(State.SUBSTRACT_AGCGZ, "DEF");
    assertToken(State.NOT_GMM, "G");
    assertToken(State.SUBSTRACT_HLK, "HIJ");
    assertToken(State.NOT_KOO, "K");
    assertToken(State.SUBSTRACT_HLK, "L");
    assertToken(State.SUBSTRACT_MON, "M");
    assertToken(State.SUBSTRACT_NPP, "NO");
    assertToken(State.SUBSTRACT_QRR, "Q");
    assertToken(State.NOT_R, "R");
    assertToken(State.SUBSTRACT_PLU, "S");
    assertToken(State.INTER_PLN, "T");
    assertToken(State.INTER_TUV_UA, "U");
    assertToken(State.INTER_TUV_AV, "V");
    assertToken(State.INTER_VW_WA, "W");
    assertToken(State.INTER_XXX_XYZ, "X");
    assertToken(State.INTER_AYZ_YMB, "Y");
    assertToken(State.INTER_ZR_ABZ, "Z");
    assertEof();
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
    assertToken(State.NOT_KOO, "-K-");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.NOT_GMM, "G");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.SUBSTRACT_MON, "-MOO");
    assertToken(State.SUBSTRACT_NPP, "N");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.SUBSTRACT_NPP, "NOON");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.SUBSTRACT_QRR, "QQQ");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.NOT_R, "RRR");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.SUBSTRACT_PLU, "SSS");
    assertEof();
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
    assertToken(State.INTER_PLN, "TTT");
    assertToken(State.INTER_TUV_UA, "-UUU-");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.INTER_TUV_AV, "VVV");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.INTER_VW_WA, "WWW");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.INTER_XXX_XYZ, "XXX");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.INTER_AYZ_YMB, "-YYY-");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.INTER_ZR_ABZ, "ZZZ");
    assertToken(State.NOT_KOO, "-");
    assertEof();
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
    assertToken(State.NOT_KOO, "-");
    assertToken(State.INTER_PUNCTUATION1, ",,,");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.INTER_PUNCTUATION2, "!!!");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.SYM_1, "&+&+&");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.SYM_2, "//..");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.SYM_3, "(-(((-");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.SYM_4, "-]");
    assertToken(State.INTER_PUNCTUATION2, "!");
    assertToken(State.SYM_5, "$```$");
    assertToken(State.INTER_PUNCTUATION2, "!");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.SYM_6, "_!_||_!_");
    assertToken(State.NOT_KOO, "-");
    assertToken(State.SYM_7, "-@@@-");
    assertToken(State.SYM_8, "-\\\\\\-");
    assertToken(State.SYM_2, "/");
    assertToken(State.SYM_9, "\";\"");
    assertToken(State.SYM_2, "/");
    assertEof();
  }

  private static CharclassScanner createScanner(String content) throws IOException {
    return new CharclassScanner(new StringReader(content));
  }
}
