/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>                    *
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>               *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


import java_cup.runtime.Symbol;

%%

%class Lexer
%cup
%implements sym

%{
  SymTab symtab;          // external symbol table

  public void setSymtab(SymTab symtab) {
    this.symtab = symtab;
  }

  private Symbol sym(int sym) {
    return new Symbol(sym);
  }

  private Symbol sym(int sym, Object val) {
    return new Symbol(sym, val);
  }
%}

%%

"arguments"     { return sym(ARGUMENTS); }
"input"         { return sym(INPUT); }
"functions"     { return sym(FUNCTIONS); }
"output"        { return sym(OUTPUT); }
"end"           { return sym(END); }
"if"            { return sym(IF); }
"then"          { return sym(THEN); }
"else"          { return sym(ELSE); }
"fi"            { return sym(FI); }
[a-z]+          { symtab.enter(yytext(),new SymtabEntry(yytext()));
                  return sym(ID,yytext()); }
[0-9]+          { return sym(NUMBER,yytext()); }
","             { return sym(COMMA); }
"("             { return sym(LPAR); }
")"             { return sym(RPAR); }
"="             { return sym(EQ); }
"-"             { return sym(MINUS); }
"+"             { return sym(PLUS); }
"*"             { return sym(TIMES); }
"/"             { return sym(DIV); }
"<"             { return sym(LE); }
"<="            { return sym(LEQ); }
[\ \t\b\f\r\n]+ { /* eat whitespace */ }
"//"[^\n]*      { /* one-line comment */ }
.               { throw new Error("Unexpected character ["+yytext()+"]"); }
