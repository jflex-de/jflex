package jflex.testcase.buffer;

import java.io.FileReader;
import java.io.IOException;

%%

%class EatAllScanner

%line
%column
%public
%int
%16bit

%standalone

%%

<YYINITIAL>.|[ \t\r\n\f] { /* Eat the tags */ }
