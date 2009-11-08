package jflex;

/**
 * 
 */
%%

%final
%public
%class UnicodeDataScanner
%ctorarg UnicodeVersion unicodeVersion

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

%{
  private static final String GENERAL_CATEGORY = "General_Category";
  UnicodeVersion unicodeVersion;
  int codePoint = -1;
  int startCodePoint = -1;
  int prevCodePoint = 0;
  int assignedStartCodePoint = -1;
  int assignedEndCodePoint = -1;
  String genCatPropValue;
  String prevGenCatPropValue;
  String uppercaseMapping = null;
  String lowercaseMapping = null;
  String titlecaseMapping = null;
  boolean isLastInRange = false;
  
  public void handleEntry() {
    if (codePoint != -1) {
      unicodeVersion.addCaselessMatches
        (codePoint, uppercaseMapping, lowercaseMapping, titlecaseMapping);

        // UnicodeData-1.1.5.txt does not list the end point for the Unified Han
        // range (starting point is listed as U+4E00).  This is U+9FFF according
        // to <http://unicode.org/Public/TEXT/OLDAPIX/CHANGES.TXT>:
        //
        //    U+4E00 ^ U+9FFF		20,992	I-ZONE Ideographs
        //
        // U+4E00 is listed in UnicodeData-1.1.5.txt as having the "Lo" property
        // value, as are the previous code points, so to include
        // [ U+4E00 - U+9FFF ], this interval should be extended to U+9FFF.
        if (assignedEndCodePoint  == 0x4E00
            && unicodeVersion.majorMinorVersion.equals("1.1")) {
          assignedEndCodePoint = 0x9FFF;
        }

      if (assignedStartCodePoint == -1) {
        assignedStartCodePoint = startCodePoint;
      } else if (codePoint > assignedEndCodePoint + 1 && ! isLastInRange) {
        unicodeVersion.addInterval
          ("Assigned", assignedStartCodePoint, assignedEndCodePoint);
        unicodeVersion.addInterval(GENERAL_CATEGORY, "Cn", 
                                   assignedEndCodePoint + 1, codePoint - 1);
        assignedStartCodePoint = codePoint;
      }
      assignedEndCodePoint = codePoint;

      if (startCodePoint != -1
          && prevGenCatPropValue.length() > 0
          && (((codePoint > prevCodePoint + 1) && ! isLastInRange)
              || ! genCatPropValue.equals(prevGenCatPropValue))) {
        // UnicodeData-1.1.5.txt does not list the end point for the Unified Han
        // range (starting point is listed as U+4E00).  This is U+9FFF according
        // to <http://unicode.org/Public/TEXT/OLDAPIX/CHANGES.TXT>:
        //
        //    U+4E00 ^ U+9FFF		20,992	I-ZONE Ideographs
        //
        // U+4E00 is listed in UnicodeData-1.1.5.txt as having the "Lo" property
        // value, as are the previous code points, so to include
        // [ U+4E00 - U+9FFF ], this interval should be extended to U+9FFF.
        if (prevCodePoint == 0x4E00
            && unicodeVersion.majorMinorVersion.equals("1.1")) {
          prevCodePoint = 0x9FFF;
        }
        unicodeVersion.addInterval
          (GENERAL_CATEGORY, prevGenCatPropValue, startCodePoint, prevCodePoint);
        startCodePoint = -1;
      }
      if (genCatPropValue.length() > 0 && startCodePoint == -1) {
        startCodePoint = codePoint;
      }
      prevCodePoint = codePoint;
      prevGenCatPropValue = genCatPropValue;
      reset();
    }
  }
  
  public void reset() {
    isLastInRange = false;
    uppercaseMapping = null;
    lowercaseMapping = null;
    titlecaseMapping = null;
    codePoint = -1;
  }
  
  public void handleFinalInterval() {
    if (startCodePoint != -1 && prevGenCatPropValue.length() > 0) {
      unicodeVersion.addInterval
        (GENERAL_CATEGORY, prevGenCatPropValue, startCodePoint, prevCodePoint);
    }
    
    // Handle the final Assigned interval
    unicodeVersion.addInterval
      ("Assigned", assignedStartCodePoint, assignedEndCodePoint);
    
    // Round max code point up to end-of-plane.
    unicodeVersion.setMaximumCodePoint(((prevCodePoint + 0x800) & 0xFFF000) - 1);
    
    // Handle the final Unassigned (Cn) interval, if any
    if (assignedEndCodePoint < unicodeVersion.getMaximumCodePoint()) {
      unicodeVersion.addInterval(GENERAL_CATEGORY, "Cn", 
                                 assignedEndCodePoint + 1, 
                                 unicodeVersion.getMaximumCodePoint());
    }
  }
%}

%init{
  this.unicodeVersion = unicodeVersion;
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
