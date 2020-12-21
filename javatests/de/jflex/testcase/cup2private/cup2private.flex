package jflex.testcase.cup2private;

%%

%public
%class Cup2PrivateScanner

%cup2
%apiprivate

%debug

%%

\s  {}
\w+  { return new ScannerToken(Token.WORD); }
[^] { return new ScannerToken(Token.OTHER);  }

<<EOF>> { return new ScannerToken(Token.EOF); }
