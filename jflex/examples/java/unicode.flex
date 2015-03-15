/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 1998-2015  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


/* �3.3 of the Java Language Specification :

UnicodeInputCharacter:

             UnicodeEscape

             RawInputCharacter

     UnicodeEscape:

             \ UnicodeMarker HexDigit HexDigit HexDigit HexDigit

     UnicodeMarker:

             u

             UnicodeMarker u

     RawInputCharacter:

             any Unicode character

     HexDigit: one of

             0 1 2 3 4 5 6 7 8 9 a b c d e f A B C D E F

only an even number of '\' is eligible to start a Unicode escape sequence

*/

import java.io.*;

%%

%public
%final
%class UnicodeEscapes
%extends FilterReader

%int
%function read

%16bit

UnicodeEscape   = {UnicodeMarker} {HexDigit} {4}
UnicodeMarker   = "u"+
HexDigit        = [0-9a-fA-F]

%state DIGITS

%init{
  super(in);
%init}

%{
  private boolean even;
 
  private int value() {
    int r = 0;

    for (int k = zzMarkedPos-4; k < zzMarkedPos; k++) {
      int c = zzBuffer[k];

      if (c >= 'a') 
        c-= 'a'-10;
      else if (c >= 'A')
        c-= 'A'-10;
      else
        c-= '0';

      r <<= 4;
      r += c;
    }
   
    return r;
  }

  public int read(char cbuf[], int off, int len) throws IOException {
    if ( !ready() ) return -1;

    len+= off;

    for (int i=off; i<len; i++) {
      int c = read();

      if (c < 0) 
        return i-off;
      else 
        cbuf[i] = (char) c;
    }

    return len-off;
  }

  public boolean markSupported() { 
    return false; 
  }

  public boolean ready() throws IOException {
    return !zzAtEOF && (zzCurrentPos < zzEndRead || zzReader.ready());
  }

%}

%%

<YYINITIAL> {
  \\               { even = false; return '\\'; }
  \\ / \\          { even = !even; return '\\'; }
  \\ / "u"         { 
                     if (even) {
                       even = false;
                       return '\\';
                     }
                     else
                       yybegin(DIGITS);
                   } 
  [^]              { return zzBuffer[zzStartRead]; }

  <<EOF>>          { return -1; }
}

<DIGITS> {
  {UnicodeEscape}  { yybegin(YYINITIAL); return value();  }
  [^]              { throw new Error("incorrect Unicode escape"); }

  <<EOF>>          { throw new Error("EOF in Unicode escape"); }
}
