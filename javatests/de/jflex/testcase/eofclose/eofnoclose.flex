package de.jflex.testcase.eofclose;

%%

%public
%class EofNoClose
%int

%eofclose false

%%

.+  { /* ignore */ }
\n  { /* that too */ }
