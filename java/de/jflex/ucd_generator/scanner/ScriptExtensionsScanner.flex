package de.jflex.ucd_generator.scanner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import de.jflex.ucd_generator.model.UnicodeData;

%%

%public
%class ScriptExtensionsScanner
%extends AbstractScriptExtensionsScanner
%ctorarg UnicodeData unicodeData

%unicode
%eofclose

%state BEGIN_RANGE, END_RANGE, PROPERTY_NAME, PROPERTY_VALUE
%state COMMENT_LINE

%int
%function scan


%init{
  super(unicodeData);
%init}

Hex = [0-9A-Fa-f]{4,6}
Spaces = [ \t]*
NL = \n | \r | \r\n
ItemSeparator = {Spaces} ";" {Spaces}

%%

<YYINITIAL> {
  /* # Property:	Script_Extensions */
  {Spaces} "#" {Spaces} "Property:" {Spaces} { yybegin(PROPERTY_NAME); }

  {Spaces} "#" { yybegin(COMMENT_LINE); }
  
  {Spaces} {NL} { }
  
  {Hex} { start = Integer.parseInt(yytext(), 16); yybegin(BEGIN_RANGE); }
}

<COMMENT_LINE> {
  .* {NL}? { yybegin(YYINITIAL); }
}

<BEGIN_RANGE> {
  ".." { yybegin(END_RANGE); }
  
  {ItemSeparator} { end = start; yybegin(PROPERTY_VALUE); }
}

<END_RANGE> {
  {Hex} { end = Integer.parseInt(yytext(), 16); }
  
  {ItemSeparator} { yybegin(PROPERTY_VALUE); }
}

<PROPERTY_NAME> {  /* # Property:	Script_Extensions */
  [^ \t\r\n]+   { propertyName = yytext(); }

  {Spaces} {NL} { yybegin(YYINITIAL); }
}

<PROPERTY_VALUE> { /* 060C          ; Arab Syrc Thaa # Po       ARABIC COMMA */
  {Spaces} [^ \t\r\n#;]+ { String script = yytext().trim();
                           addScript(script);
                         }

  {Spaces} ("#" .*)? {NL} { yybegin(YYINITIAL); }
}

<YYINITIAL,PROPERTY_NAME,PROPERTY_VALUE> { 
  <<EOF>> { addPropertyValueIntervals(); return 0; }
}
