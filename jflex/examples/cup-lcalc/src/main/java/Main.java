/*
  Copyright 1999, Richard A. Sevenich, Christopher Lopes
  Copyright 2003, Gerwin Klein

  SPDX-License-Identifier: BSD-3-Clause

  This example comes from a short article series in the Linux Gazette by Richard
  A. Sevenich and Christopher Lopes, titled "Compiler Construction Tools".

  Small changes and updates to newest JFlex+Cup versions by Gerwin Klein
*/

/*
  Commented By: Christopher Lopes

  <p>To Run:
  <pre>
  java Main test.txt
  </pre>
  where {@code test.txt} is an test input file for the calculator.
*/

import java.io.*;

public class Main {
  public static void main(String[] argv) {
    /* Start the parser */
    try {
      parser p = new parser(new Lexer(new FileReader(argv[0])));
      Object result = p.parse().value;
    } catch (Exception e) {
      /* do cleanup here -- possibly rethrow e */
      e.printStackTrace();
    }
  }
}
