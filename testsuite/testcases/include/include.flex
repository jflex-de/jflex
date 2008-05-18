import java.io.*;

%%

%public
%class Include
%standalone

WSP   = [ \t\b]
ALPHA = [a-zA-Z]

NAMEPART = ({ALPHA}|[0-9])+

FILENAME = {NAMEPART} ("/" {NAMEPART})*

%%

"%include" {WSP}+ {FILENAME} {
  System.out.println("including \""+yytext().substring(9).trim()+"\"");
  yypushStream( new FileReader(yytext().substring(9).trim()) );
}

"token" {WSP}+ .    { System.out.println("Token \""+yytext().substring(6).trim()+"\""); }

\n|\r\n|\r          { System.out.println("newline"); }

<<EOF>>  { System.out.println("<<EOF>>"); if (yymoreStreams()) yypopStream(); else return YYEOF; }
 
