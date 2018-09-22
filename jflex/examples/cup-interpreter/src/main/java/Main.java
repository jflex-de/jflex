/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>                    *
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>               *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


import java.io.*;

/**
 * Main program of the interpreter for the AS programming language.
 * Based on JFlex/CUP.
 * 
 * Steps:
 * - scanning                               (Yylex)
 * - context free parsing and AST building  (yyparse)
 * - build up symbol table                  (setSymtabs)
 * - check context conditions               (checkcontext)
 * - prepare interpretation                 (prepInterp)
 * - start interpretation                   (interpret)
 */ 
public class Main {

  public static void main(String [] args) throws Exception {
    Reader reader = null;
    
    if (args.length == 1) {
      File input = new File(args[0]);
      if (!input.canRead()) {
        System.out.println("Error: could not read ["+input+"]");
      }
      reader = new FileReader(input);
    }
    else {  
     reader = new InputStreamReader(System.in);
    }

    Yylex scanner = new Yylex(reader);   // create scanner
    SymTab symtab = new SymTab();        // set global symbol table    
    scanner.setSymtab(symtab);

    parser parser = new parser(scanner); // create parser
    Tprogram syntaxbaum = null;

    try { 
      syntaxbaum = (Tprogram) parser.parse().value;  // parse
    }    
    catch (Exception e) { 
      e.printStackTrace(); 
    }

    // System.out.println(symtab);
    System.out.println(syntaxbaum);

    syntaxbaum.setSymtabs();          // set symbol table
    // syntaxbaum.printSymtabs();       // print symbol table

    syntaxbaum.checkcontext();        // CoCo (DefVar, DefFun, Arity)
    if(contexterror>0) return;

    syntaxbaum.prepInterp();          // var. indices and function pointers
    // im Syntaxbaum setzen
    syntaxbaum.interpret();           // interpretation
  }

  static int contexterror = 0;        // number of errors in context conditions

  public static void error(String s) { 
    System.out.println((contexterror++)+". "+s); 
  }
}
