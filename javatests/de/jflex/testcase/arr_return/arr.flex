package de.jflex.testcase.arr_return;

%%

%class Arr
%type int []

%%

"blub" { return new int[2]; }

