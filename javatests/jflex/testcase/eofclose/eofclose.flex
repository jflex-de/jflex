package jflex.testcase.eofclose;

import java.io.*;

%%

%public
%class Eofclose
%int

%eofclose

%%

.+  { /* ignore */ }
\n  { /* that too */ }
