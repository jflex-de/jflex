package jflex.testcase.eofclose;

%%

%public
%class EofClose
%int

%eofclose

%%

.+  { /* ignore */ }
\n  { /* that too */ }
