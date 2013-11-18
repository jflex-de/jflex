import java.io.*;

%%

%public
%class Include2
%standalone
%char

WSP   = [ \t\b]
ALPHA = [a-zA-Z]

NAMEPART = ({ALPHA}|[0-9])+

FILENAME = {NAMEPART} ("/" {NAMEPART})*

%%

"%include" {WSP}+ \" {FILENAME} \" {
  String quotedFilename = yytext().substring(9).trim();
  System.out.println("including " + quotedFilename);
  yypushStream(new FileReader(quotedFilename.substring(1, quotedFilename.length() - 1)));
}

^ "token" {WSP}+ .    { System.out.println("Token \""+yytext().substring(6).trim()+"\" at char #"+yychar); }

\n|\r\n|\r          { System.out.println("newline"); }

<<EOF>>  { System.out.println("<<EOF>>"); if (yymoreStreams()) yypopStream(); else return YYEOF; }
 
