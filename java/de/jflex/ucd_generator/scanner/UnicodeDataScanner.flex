package de.jflex.ucd_generator.scanner;

import de.jflex.ucd_generator.model.UnicodeData;
import de.jflex.ucd_generator.ucd.UcdVersion;

/**
 * Scanner of the {@code UnicodeData.txt}.
 */
%%

%final
%public
%class UnicodeDataScanner
%extends AbstractUnicodeDataScanner
%ctorarg UcdVersion ucdVersion
%ctorarg UnicodeData unicodeData

%unicode
%eofclose

%state NAME_1, GENERAL_CATEGORY_2, CANONICAL_COMBINING_CLASS_3, BIDI_CLASS_4
%state DECOMPOSITION_TYPE_AND_MAPPING_5, NUMERIC_TYPE_6, NUMERIC_VALUE_INTEGER_7
%state NUMERIC_VALUE_OTHER_8, BIDI_MIRRORED_9, UNICODE_1_NAME_10, ISO_COMMENT_11
%state SIMPLE_UPPERCASE_MAPPING_12, SIMPLE_LOWERCASE_MAPPING_13
%state SIMPLE_TITLECASE_MAPPING_14

%state IGNORE_REST_OF_LINE

%int
%function scan

%init{
  super(ucdVersion, unicodeData);
%init}

Hex = [0-9A-Fa-f]+
Spaces = [ \t]*
NL = \n | \r | \r\n

%%

<YYINITIAL> { 
  {Spaces} ("#" .*)? {NL} { }
 
  // Code point field (Field #0)
  {Hex} { codePoint = Integer.parseInt(yytext(), 16); }
  
  ";" { yybegin(NAME_1); }

  <<EOF>> { handleFinalInterval(); return 0; }
}

<NAME_1> {
  "<" [^>,]+ "," {Spaces} "Last>" { isLastInRange = true; }
  
  [^;]+ { }
  
  ";" { yybegin(GENERAL_CATEGORY_2); }
}

<GENERAL_CATEGORY_2> {
  [^;]+ { genCatPropValue = yytext(); }
   
  ";" { yybegin(CANONICAL_COMBINING_CLASS_3); }
}

<IGNORE_REST_OF_LINE> {
  .* { reset(); yybegin(YYINITIAL); }
}

<CANONICAL_COMBINING_CLASS_3> {
  [^;]+ { }
  
  ";" { yybegin(BIDI_CLASS_4); }
}

<BIDI_CLASS_4> {
  [^;]+ { }
  
  ";" { yybegin(DECOMPOSITION_TYPE_AND_MAPPING_5); }
}

<DECOMPOSITION_TYPE_AND_MAPPING_5> {
  [^;]+ { }
  
  ";" { yybegin(NUMERIC_TYPE_6); }
}

<NUMERIC_TYPE_6> {
  [^;]+ { }
  
  ";" { yybegin(NUMERIC_VALUE_INTEGER_7); }
}

<NUMERIC_VALUE_INTEGER_7> {
  [^;]+ { }
  
  ";" { yybegin(NUMERIC_VALUE_OTHER_8); }
}

<NUMERIC_VALUE_OTHER_8> {
  [^;]+ { }
  
  ";" { yybegin(BIDI_MIRRORED_9); }
}

<BIDI_MIRRORED_9> {
  [^;]+ { }
  
  ";" { yybegin(UNICODE_1_NAME_10); }
}

<UNICODE_1_NAME_10> {
  [^;]+ { }
  
  ";" { yybegin(ISO_COMMENT_11); }
}

<ISO_COMMENT_11> {
  [^;]+ { }
  
  ";" { yybegin(SIMPLE_UPPERCASE_MAPPING_12); }
}

<SIMPLE_UPPERCASE_MAPPING_12> {
  [^;]+ { uppercaseMapping = yytext(); }
  
  ";" { yybegin(SIMPLE_LOWERCASE_MAPPING_13); }
}

<SIMPLE_LOWERCASE_MAPPING_13> {
  [^;]+ { lowercaseMapping = yytext(); }
  
  ";" { yybegin(SIMPLE_TITLECASE_MAPPING_14); }
}

<SIMPLE_TITLECASE_MAPPING_14> {
  [^\r\n]+ { titlecaseMapping = yytext(); }
  
  {NL} { handleEntry(); yybegin(YYINITIAL); }
  
  <<EOF>> { handleEntry(); handleFinalInterval(); return 0; }
}
