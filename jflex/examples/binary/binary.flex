/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 1998-2009  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


import java.io.*;

%%

%class BinaryLexer
%8bit

%int

%{
  public static void main(String [] argv) {
    for (int i = 0; i < argv.length; i++) {
      try {
        System.out.print("["+argv[i]+"] is ");
        BinaryLexer l = new BinaryLexer(new StraightStreamReader(new FileInputStream(argv[i])));
        l.yylex();
      }
      catch (Exception e) {
        e.printStackTrace(System.out);
        System.exit(1);
      }
    }
  }
%}

magic = \xCA \xFE \xBA \xBE

%%

{magic} [^]+  { System.out.println("a class file"); }

[^]+          { System.out.println("not a class file"); }
