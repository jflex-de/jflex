/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 1998-2015  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import java.io.*;


/**
 * Simple test driver for the java parser. Just runs it on some
 * input files, gives no useful output.
 */
public class JavaParser {

  public static void main(String argv[]) {

    for (int i = 0; i < argv.length; i++) {
      try {
        System.out.println("Parsing ["+argv[i]+"]");
        Scanner s = new Scanner(new UnicodeEscapes(new FileReader(argv[i])));
        parser p = new parser(s);
        p.parse();
        
        System.out.println("No errors.");
      }
      catch (Exception e) {
        e.printStackTrace(System.out);
        System.exit(1);
      }
    }
  }

}
