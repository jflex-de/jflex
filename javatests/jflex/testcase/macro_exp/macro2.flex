package jflex.testcase.macro_exp2;

%%

MACRO = "using some " {UNDEFINED} " macro"

%%

{MACRO}  { /* some action */ }
