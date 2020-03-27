
import java.io.*;

%%

%public
%class Eof
%int

%debug

%eof{
  System.out.println("EOF");

  // should never throw, but the compiler won't know
  if (System.out == null) {
    throw new java.io.IOException("testing");
  }
%eof}

%eofthrow{
   java.io.IOException
%eofthrow}

%%

.+  { /* ignore */ }
\n  { /* that too */ }

<<EOF>> { System.out.println("<<EOF>>"); return -1; }