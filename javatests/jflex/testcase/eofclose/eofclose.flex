package jflex.testcase.eofclose;

%%

%public
%class Eofclose
%int

%eofclose

%%

.+  { /* ignore */ }
\n  { /* that too */ }
