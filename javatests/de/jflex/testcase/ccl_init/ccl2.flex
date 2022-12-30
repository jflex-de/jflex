package de.jflex.testcase.ccl_init;

// should fail to generate
// see https://github.com/jflex-de/jflex/issues/986

%%

%public
%class Ccl2
%int

%full
x = ab
// double declaration of char set should error
%unicode

%%

{x} { return 0; }
[^] { return 1; }
