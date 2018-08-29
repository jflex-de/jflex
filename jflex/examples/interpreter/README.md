FJlex complex example (with CUP)
================================

This directory contains an interpreter for a small functional
programming language (called "AS"). It uses JFlex and CUP.

## Files

- `Main.java` is the main program. It reads an AS program from
  std in and interprets it.
- Classes beginning with "T" implement the abstract syntax tree.
  They also contain context condition checking and the interpreter.
- `Symtab.java`, `SymtabEntry.java, STEfun.java, STEvar.java implement
  the symbol table.
- `scanner.lex` and `parser.cup` contain the scanner and parser.
- `example.as` is an example program in AS

'ant run' or the Makefile runs jflex, cup, javac and the
compiled interpreter


The language is described in:
Manfred Broy: _Einfuehrung in die Informatik_, Springer Verlag 

The files are a solution to excercise 3.40 in the book:
Manfred Broy,Bernhard Rumpe: 
_Uebungen zur Einfuehrung in die Informatik --                      
Strukturierte Aufgabensammlung mit Musterloesungen_, 
Springer Verlag, 2001

Both books are only available in German. Sorry.
